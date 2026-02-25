package tea4life.product_service.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tea4life.product_service.config.database.SnowflakeGenerated;
import tea4life.product_service.model.base.BaseEntity;

import java.math.BigDecimal;

/**
 * @author Le Tran Gia Huy
 * @created 06/02/2026 - 5:04 PM
 * @project Tea4Life-Product-Service
 * @package tea4life.product_service.model.product
 */

@Entity
@Data
@Table(name = "purchase_receipt_details")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseReceiptDetail extends BaseEntity {
    @Id
    @EqualsAndHashCode.Include
    @SnowflakeGenerated
    Long id;
    @Column(nullable = false)
    Long quantity;
    @Column(name = "unit_price", nullable = false)
    BigDecimal unitPrice;
    @Column(name = "sub_total", nullable = false)
    BigDecimal subTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_receipt_id", nullable = false)
    PurchaseReceipt purchaseReceipt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    Product product;
}
