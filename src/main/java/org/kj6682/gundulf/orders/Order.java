package org.kj6682.gundulf.orders;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.kj6682.commons.LocalDateDeserializer;
import org.kj6682.commons.LocalDateSerializer;
import org.kj6682.gundulf.orders.orderline.OrderLine;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.util.Assert.hasLength;
import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

@Data
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String customer;
    private String address;
    private String shop;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate created;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate deadline;


    @OneToMany(cascade = CascadeType.ALL,
               orphanRemoval = true)
    private Set<Product> products = new HashSet<>();

    protected  Order(){};

    public Order(String customer,
                 String address,
                 String shop,
                 LocalDate created,
                 LocalDate deadline,
                 Set<Product> products) {
        notNull(customer, "an order needs a customer");
        notNull(address, "an order needs an address for delivery");
        notNull(shop, "an order must be attached to a shop");
        notNull(created, "an order must fixed creation date");
        notNull(deadline, "an order must have a fixed deadline");
        notNull(products, "an order must have a collection of products");

        this.customer = customer;
        this.address = address;
        this.shop = shop;
        this.created = created;
        this.deadline = deadline;
        this.products = products;
    }
}
