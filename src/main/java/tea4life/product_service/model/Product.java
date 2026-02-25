package tea4life.product_service.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tea4life.product_service.config.database.SnowflakeGenerated;
import tea4life.product_service.model.base.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 06/02/2026 - 4:04 PM
 * @project Tea4Life-Product-Service
 * @package tea4life.product_service.model.product
 */

@Entity
@Data
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product extends BaseEntity {
    @Id
    @EqualsAndHashCode.Include
    @SnowflakeGenerated
    Long id;
    @Column(name = "in_stock", nullable = false)
    Integer inStock;
    @Column(nullable = false)
    String description;
    @Column(name = "thumbnail_key", nullable = false)
    String thumbnailKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_category_id")
    ProductCategory productCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_region_id")
    ProductRegion productRegion;

    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude
    List<ProductPrice> productPrices = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude
    List<PurchaseReceiptDetail> purchaseReceiptDetails = new ArrayList<>();

}
