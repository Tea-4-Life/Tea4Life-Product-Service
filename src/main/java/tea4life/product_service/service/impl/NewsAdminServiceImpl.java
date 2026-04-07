package tea4life.product_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tea4life.product_service.dto.base.PageResponse;
import tea4life.product_service.dto.request.NewsChunkRequest;
import tea4life.product_service.dto.request.NewsRequest;
import tea4life.product_service.dto.response.NewsDetailResponse;
import tea4life.product_service.dto.response.NewsSummaryResponse;
import tea4life.product_service.model.News;
import tea4life.product_service.model.NewsCategory;
import tea4life.product_service.model.NewsChunk;
import tea4life.product_service.repository.news.NewsCategoryRepository;
import tea4life.product_service.repository.news.NewsRepository;
import tea4life.product_service.service.NewsAdminService;
import tea4life.product_service.utils.NewsMapper;

/**
 * @author Le Tran Gia Huy
 * @created 07/04/2026 - 2:27 PM
 * @project Tea4Life-Product-Service
 * @package tea4life.product_service.service.impl
 */

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsAdminServiceImpl implements NewsAdminService {
    private final NewsRepository newsRepository;
    private final NewsCategoryRepository categoryRepository;
    private final NewsMapper newsMapper;

    public PageResponse<NewsSummaryResponse> findAll(Pageable pageable) {
        Page<@NonNull NewsSummaryResponse> responsePage = newsRepository.findAllNewsWithCategory(pageable)
                .map(newsMapper::mapToSummaryResponse);
        return new PageResponse<>(responsePage);
    }

    public PageResponse<NewsSummaryResponse> findByCategoryId(Long categoryId, Pageable pageable) {
        Page<@NonNull NewsSummaryResponse> responsePage = newsRepository
                .findAllByCategoryId(categoryId, pageable)
                .map(newsMapper::mapToSummaryResponse);
        return new PageResponse<>(responsePage);
    }

    public NewsDetailResponse findById(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài viết!"));
        return newsMapper.mapToDetailResponse(news);
    }

    public NewsDetailResponse findBySlug(String slug) {
        News news = newsRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài viết!"));
        return newsMapper.mapToDetailResponse(news);
    }

    @Transactional
    public NewsDetailResponse create(NewsRequest request) {
        // Luồng Create: Khởi tạo Entity mới tinh, bỏ qua hoàn toàn khái niệm ID
        News news = new News();
        return processAndSaveNews(news, request);
    }

    @Transactional
    public NewsDetailResponse update(Long id, NewsRequest request) {
        // Luồng Update: Bắt buộc ID phải tồn tại, nếu sai ném lỗi 404 ngay lập tức
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài viết để cập nhật!"));

        return processAndSaveNews(news, request);
    }

    @Transactional
    public void delete(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài viết để xóa!"));
        newsRepository.delete(news);
    }

    // Hàm private chứa core logic
    private NewsDetailResponse processAndSaveNews(News news, NewsRequest request) {
        // 1. Cập nhật thông tin cơ bản
        news.setTitle(request.title());
        news.setThumbnailUrl(request.thumbnailUrl());

        // 2. Cập nhật Category nếu có thay đổi
        boolean isCategoryChanged = (news.getCategory() == null) ||
                (!news.getCategory().getId().equals(request.categoryId()));

        if (isCategoryChanged) {
            NewsCategory category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Danh mục không tồn tại"));
            news.setCategory(category);
        }

        // 3. XỬ LÝ CHUNKS
        // An toàn vì ở Entity News bạn đã có sẵn: private List<NewsChunk> chunks = new ArrayList<>();
        news.getChunks().clear();

        for (NewsChunkRequest chunkReq : request.chunks()) {
            NewsChunk newChunk = new NewsChunk();
            newChunk.setType(chunkReq.type());
            newChunk.setContent(chunkReq.content());
            newChunk.setSortIndex(chunkReq.sortIndex());
            news.addChunk(newChunk); // Dùng hàm helper để link 2 chiều
        }

        // 4. Lưu xuống DB và trả về response
        news = newsRepository.save(news);
        return newsMapper.mapToDetailResponse(news);
    }
}
