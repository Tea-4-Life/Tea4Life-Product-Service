package tea4life.product_service.model.product;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tea4life.product_service.generator.SnowflakeGenerated;
import tea4life.product_service.model.base.BaseEntity;
import tea4life.product_service.model.product.constant.PriceType;

import java.math.BigDecimal;

/**
 * @author Le Tran Gia Huy
 * @created 06/02/2026 - 4:49 PM
 * @project Tea4Life-Product-Service
 * @package tea4life.product_service.model.product
 */

@Entity
@Data
@Table(name = "product_prices")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductPrice extends BaseEntity {
    @Id
    @EqualsAndHashCode.Include
    @SnowflakeGenerated
    Long id;
    @Column(nullable = false)
    BigDecimal price;
    @Column(name = "price_type", nullable = false)
    @Enumerated(EnumType.STRING)
    PriceType priceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    Supplier supplier;
}
