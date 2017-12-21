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
import java.time.format.DateTimeFormatter;

@Data
@Entity
class OrderLine {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String shop;
    private String producer;
    private String product;
    private Integer quantity;
    private Integer executed;
    private String status;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate created;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate deadline;


    protected OrderLine() {
    }

    public OrderLine(String shop, String producer, String product, Integer quantity, LocalDate created, LocalDate deadline) {
        this.shop = shop;
        this.producer = producer;
        this.product = product;
        this.quantity = quantity;
        this.created = created;
        this.deadline = deadline;
        this.status = "NEW";
        this.executed = 0;
    }

    public String getDeadLineAndProduct(){

        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return this.getDeadline().format(formatters).concat(this.getProduct());
    }
}//:)
