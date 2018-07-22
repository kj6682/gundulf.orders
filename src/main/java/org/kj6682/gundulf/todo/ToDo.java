package org.kj6682.gundulf.todo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.kj6682.commons.LocalDateDeserializer;
import org.kj6682.commons.LocalDateSerializer;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"product", "size", "quantity"})
})
public class ToDo {

    @Id
    @GeneratedValue
    Long id;

    String product;

    Integer size;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    LocalDate deadline;

    Integer quantity;

    public ToDo(String product, Integer size, LocalDate deadline, Integer quantity) {

        this.deadline = deadline;
        this.size = size;
        this.product = product;
        this.quantity = quantity;
    }

    public ToDo() {
    }


}//:)
