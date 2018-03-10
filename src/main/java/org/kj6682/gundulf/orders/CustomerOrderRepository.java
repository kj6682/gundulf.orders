package org.kj6682.gundulf.orders;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerOrderRepository extends CrudRepository<CustomerOrder, Long> {
    List<CustomerOrder> findAll();
}
