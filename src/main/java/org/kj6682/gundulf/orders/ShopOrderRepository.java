package org.kj6682.gundulf.orders;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ShopOrderRepository extends CrudRepository<ShopOrder, Long> {
    List<ShopOrder> findAll();
    
    void delete(Long id);
}
