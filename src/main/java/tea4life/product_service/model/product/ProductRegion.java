package tea4life.product_service.model.product;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tea4life.product_service.config.database.SnowflakeGenerated;
import tea4life.product_service.model.base.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 06/02/2026 - 4:27 PM
 * @project Tea4Life-Product-Service
 * @package tea4life.product_service.model.product
 */

@Entity
@Data
@Table(name = "product_regions")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRegion extends BaseEntity {
    @Id
    @EqualsAndHashCode.Include
    @SnowflakeGenerated
    Long id;
    String name;
    @Column(nullable = false)
    String country;
    @Column(nullable = false)
    String province;
    @Column(nullable = false)
    String description;

    @OneToMany(mappedBy = "productRegion", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude
    List<Product> products = new ArrayList<>();
}
