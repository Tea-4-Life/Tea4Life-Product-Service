package tea4life.product_service.dto.event;

/**
 * @author : user664dntp
 * @mailto : phatdang19052004@gmail.com
 * @created : 19/03/2026, Thursday
 **/
public record ProductDeletedAuditEvent(
        Long productId,
        String productName,
        String action,
        Long performedBy,
        long timestamp,
        String message
) {}
