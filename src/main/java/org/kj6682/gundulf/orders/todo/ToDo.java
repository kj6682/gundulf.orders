package org.kj6682.gundulf.orders.todo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.kj6682.commons.LocalDateDeserializer;
import org.kj6682.commons.LocalDateSerializer;

import java.time.LocalDate;

public class ToDo {

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    LocalDate deadline;
    String product;
    Integer quantity;

    public ToDo(LocalDate deadline, String product, Long quantity) {
        this.deadline = deadline;
        this.product = product;
        this.quantity = quantity.intValue();
    }

    public ToDo() {
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ToDo{");
        sb.append("deadline=").append(deadline);
        sb.append(", product='").append(product).append('\'');
        sb.append(", quantity=").append(quantity);
        sb.append('}');
        return sb.toString();
    }

    static String csvHeader(){
        return "DEADLINE;PRODUCT;QUANTITY\n";
    }

    String asCsv() {
        final StringBuilder sb = new StringBuilder();
        sb.append(deadline).append(';');
        sb.append(product).append(';');
        sb.append(quantity).append(';');
        sb.append("\n");
        return sb.toString();
    }


}//:)
