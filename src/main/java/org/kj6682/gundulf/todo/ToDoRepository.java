package org.kj6682.gundulf.todo;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ToDoRepository  extends CrudRepository<ToDo, ToDoKey> {
    List<ToDo> findAll();
}
