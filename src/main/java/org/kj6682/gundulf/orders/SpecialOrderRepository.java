package org.kj6682.gundulf.orders;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SpecialOrderRepository extends CrudRepository<SpecialOrder, Long> {
    List<SpecialOrder> findAll();
}
