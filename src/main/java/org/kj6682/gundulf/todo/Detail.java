package org.kj6682.gundulf.todo;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Detail {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Long id;

    String shop;

    Integer quantity;

  //  @ManyToOne(fetch = FetchType.LAZY)
  //  private ToDo todo;

    protected Detail(){}

    Detail(String shop, Integer quantity) {
        this.shop = shop;
        this.quantity = quantity;
    }

}
