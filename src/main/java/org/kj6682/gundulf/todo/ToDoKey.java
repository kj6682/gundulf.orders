package org.kj6682.gundulf.todo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.kj6682.commons.LocalDateDeserializer;
import org.kj6682.commons.LocalDateSerializer;

import java.io.Serializable;
import java.time.LocalDate;

public class ToDoKey implements Serializable {

    @JsonSerialize
    String product;

    @JsonSerialize
    Integer size;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    LocalDate deadline;

    public ToDoKey(){}

    public ToDoKey(String product, Integer size, LocalDate deadline){
        this.product = product;
        this.size = size;
        this.deadline = deadline;
    }
}
