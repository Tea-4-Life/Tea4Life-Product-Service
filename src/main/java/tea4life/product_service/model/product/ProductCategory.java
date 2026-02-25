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
 * @created 06/02/2026 - 4:17 PM
 * @project Tea4Life-Product-Service
 * @package tea4life.product_service.model.product
 */

@Entity
@Data
@Table(name = "product_categories")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCategory extends BaseEntity {
    @Id
    @EqualsAndHashCode.Include
    @SnowflakeGenerated
    Long id;
    @Column(nullable = false)
    String name;
    @Column(nullable = false)
    String description;

    @OneToMany(mappedBy = "productCategory", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude
    List<Product> products = new ArrayList<>();
}
