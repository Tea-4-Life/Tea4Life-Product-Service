package tea4life.product_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tea4life.product_service.dto.base.PageResponse;
import tea4life.product_service.dto.response.NewsDetailResponse;
import tea4life.product_service.dto.response.NewsSummaryResponse;
import tea4life.product_service.service.NewsService;

/**
 * @author Le Tran Gia Huy
 * @created 07/04/2026 - 2:02 PM
 * @project Tea4Life-Product-Service
 * @package tea4life.product_service.controller
 */

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/public/news")
public class NewsController {
    NewsService newsService;

    // FE gọi trang chủ: /api/v1/news?page=0&size=10
    @GetMapping
    public ResponseEntity<PageResponse<NewsSummaryResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(newsService.findAll(pageable));
    }

    // FE gọi khi khách click vào đọc bài: /api/v1/news/tra-dao-cam-sa-a1b2c
    @GetMapping("/{slug}")
    public ResponseEntity<NewsDetailResponse> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(newsService.findBySlug(slug));
    }

    // FE gọi khi khách bấm vào một Danh mục trên thanh menu: /api/v1/news/category/khuyen-mai
    @GetMapping("/category/{categorySlug}")
    public ResponseEntity<PageResponse<NewsSummaryResponse>> getByCategorySlug(
            @PathVariable String categorySlug,
            Pageable pageable) {
        return ResponseEntity.ok(newsService.findByCategorySlug(categorySlug, pageable));
    }
}
