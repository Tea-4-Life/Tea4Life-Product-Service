package tea4life.product_service.model.product;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tea4life.product_service.generator.SnowflakeGenerated;
import tea4life.product_service.model.base.BaseEntity;
import tea4life.product_service.model.product.constant.ReceiptStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 06/02/2026 - 4:59 PM
 * @project Tea4Life-Product-Service
 * @package tea4life.product_service.model.product
 */

@Entity
@Data
@Table(name = "purchase_receipts")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseReceipt extends BaseEntity {
    @Id
    @EqualsAndHashCode.Include
    @SnowflakeGenerated
    Long id;
    BigDecimal total;
    @Column(name="receipt_status",nullable = false)
    @Enumerated(EnumType.STRING)
    ReceiptStatus receiptStatus;

    @OneToMany(mappedBy = "purchaseReceipt", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude
    List<PurchaseReceiptDetail> purchaseReceiptDetails = new ArrayList<>();



}
