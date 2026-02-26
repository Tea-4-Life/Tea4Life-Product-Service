package tea4life.product_service.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tea4life.product_service.config.database.SnowflakeGenerated;
import tea4life.product_service.model.base.BaseEntity;

import java.util.List;

/**
 * Admin 2/26/2026
 *
 **/
@Entity
@Table(name = "product_options")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductOption extends BaseEntity {

    @Id
    @SnowflakeGenerated
    Long id;

    String name;
    boolean isRequired;
    boolean isMultiSelect;

    @Column(name = "sort_order", nullable = false)
    Integer sortOrder = 0;

    @ManyToMany(mappedBy = "productOptions")
    List<Product> products;

    @OneToMany(mappedBy = "productOption", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("sortOrder ASC")
    List<ProductOptionValue> productOptionValues;

}
