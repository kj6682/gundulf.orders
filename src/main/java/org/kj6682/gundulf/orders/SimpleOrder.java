package org.kj6682.gundulf.orders;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.kj6682.commons.LocalDateDeserializer;
import org.kj6682.commons.LocalDateSerializer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@Entity
public class SimpleOrder {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String shop;
    private String producer;
    private String product;
    private short pieces;
    private short quantity;
    private short executed;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate created;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate deadline;

    private String status;

    protected SimpleOrder() {
    }

    public SimpleOrder(String shop, String producer, String product, short pieces, short quantity, LocalDate created, LocalDate deadline) {
        this.shop = shop;
        this.producer = producer;
        this.product = product;
        this.pieces = pieces;
        this.quantity = quantity;
        this.created = created;
        this.deadline = deadline;
        this.status = "NEW";
        this.executed = 0;
    }
}//:)
