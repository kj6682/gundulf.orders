package org.kj6682.gundulf.orders;

import lombok.Data;
import static org.springframework.util.Assert.hasLength;
import static org.springframework.util.Assert.notNull;
import static org.springframework.util.Assert.isTrue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String name;

    private String category;

    private Integer quantity;

    private Integer size;

    public Product(String name, String category, Integer quantity, Integer size) {
        hasLength(name, "a product in an order needs a name");
        hasLength(category, "a product in an order needs a category");
        notNull(quantity, "a product in an order needs a quantity");
        isTrue(quantity.intValue() >= 0, "a product in an order needs a positive quantity");
        notNull(size, "a product in an order needs a size");
        isTrue(size.intValue() >= 0, "a product in an order needs a positive size");

        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.size = size;
    }

    protected Product() {
    }
}
