package tea4life.product_service.dto.event;

import tea4life.product_service.model.enums.AuditAction;

/**
 * @author : user664dntp
 * @mailto : phatdang19052004@gmail.com
 * @created : 19/03/2026, Thursday
 **/
public record ProductAuditEvent(
        Long productId,
        String productName,
        AuditAction action,
        String performedBy,
        long timestamp,
        String message
) {}
