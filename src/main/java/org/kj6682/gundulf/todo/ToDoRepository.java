package org.kj6682.gundulf.todo;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface ToDoRepository  extends CrudRepository<ToDo, ToDoKey> {
    List<ToDo> findAll();
    List<ToDo> findByDeadline(LocalDate date);
    List<ToDo> findByProduct(String product);
}

