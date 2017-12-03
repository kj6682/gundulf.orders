package org.kj6682.gundulf.orders;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SimpleOrderRepository extends CrudRepository<SimpleOrder, Long> {


    List<SimpleOrder> findByProducerOrderByDeadline(@Param("producer") String producer);
    List<SimpleOrder> findByShopOrderByCreated(@Param("shop") String shop);

}
