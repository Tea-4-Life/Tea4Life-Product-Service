package tea4life.product_service.dto.event;

import tea4life.product_service.model.enums.AuditAction;

/**
 * @author : user664dntp
 * @mailto : phatdang19052004@gmail.com
 * @created : 20/03/2026, Friday
 **/
public record CategoryAuditEvent(
        Long categoryId,
        String categoryName,
        AuditAction action,
        String performedBy,
        long timestamp,
        String message
) { }