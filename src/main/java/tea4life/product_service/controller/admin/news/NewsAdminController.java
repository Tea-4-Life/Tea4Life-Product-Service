package tea4life.product_service.controller.admin.news;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Le Tran Gia Huy
 * @created 07/04/2026 - 2:03 PM
 * @project Tea4Life-Product-Service
 * @package tea4life.product_service.controller.admin.news
 */

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/admin/news")
public class NewsAdminController {

}
