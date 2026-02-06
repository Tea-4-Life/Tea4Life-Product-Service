package tea4life.product_service.model.product;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tea4life.product_service.generator.SnowflakeGenerated;
import tea4life.product_service.model.base.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 06/02/2026 - 4:23 PM
 * @project Tea4Life-Product-Service
 * @package tea4life.product_service.model.product
 */

@Entity
@Data
@Table(name = "suppliers")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Supplier extends BaseEntity {
    @Id
    @EqualsAndHashCode.Include
    @SnowflakeGenerated
    private Long id;
    @Column(nullable = false)
    String name;
    @Column(nullable = false)
    String description;
    @Column(name = "logo_key", nullable = false)
    String logoKey;

    @OneToMany(mappedBy = "supplier", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude
    List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "supplier", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude
    List<ProductPrice> productPrices = new ArrayList<>();

}
