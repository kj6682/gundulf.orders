package org.kj6682.gundulf.orders;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderLineRepository extends CrudRepository<OrderLine, Long> {


    List<OrderLine> findByProducerOrderByDeadline(@Param("producer") String producer);
    List<OrderLine> findByShopOrderByCreated(@Param("shop") String shop);

}
