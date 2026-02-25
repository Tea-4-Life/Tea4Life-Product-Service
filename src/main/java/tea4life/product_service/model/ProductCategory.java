package tea4life.product_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tea4life.product_service.config.database.SnowflakeGenerated;

/**
 * Admin 2/25/2026
 *
 **/
@Entity
@Table(name = "product_categories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCategory {

    @SnowflakeGenerated
    @Id
    Long id;

    @Column(unique = true, nullable = false)
    String name;

    String description;
    String iconUrl;

}
