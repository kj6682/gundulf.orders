package org.kj6682.gundulf.todo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.kj6682.commons.LocalDateDeserializer;
import org.kj6682.commons.LocalDateSerializer;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"product", "size", "quantity"})
})
public class ToDo {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Long id;

    String product;

    Integer size;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    LocalDate deadline;

    Integer quantity;

    @OneToMany(cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<Detail> details = new HashSet<>();

    protected ToDo() {
    }

    ToDo(String product, Integer size, LocalDate deadline, Integer quantity) {

        this.deadline = deadline;
        this.size = size;
        this.product = product;
        this.quantity = quantity;
        this.details = new HashSet<>();
    }

    void addDetail(Detail detail) {
        details.add(detail);
       // detail.setTodo(this);
    }

    void removeDetail(Detail detail) {
        details.remove(detail);
        //detail.setTodo(null);
    }

    void update(String shop, Integer quantity){

        if(details.stream().filter(p->p.getShop().equals(shop)).findFirst().isPresent()){
            Detail dt = details.stream().filter(p->p.getShop().equals(shop)).findFirst().get();
            dt.setQuantity(dt.getQuantity() + quantity);
        }else{
            addDetail(new Detail(shop, quantity));
        }
        
        this.quantity = this.quantity + quantity;
    }
}//:)
