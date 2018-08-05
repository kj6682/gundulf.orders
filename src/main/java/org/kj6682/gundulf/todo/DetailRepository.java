package org.kj6682.gundulf.todo;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DetailRepository extends CrudRepository<Detail, Long> {

    List<Detail> findAll();

}
