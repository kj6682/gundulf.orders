package org.kj6682.gundulf.orders;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderLineRepository extends CrudRepository<OrderLine, Long> {


    List<OrderLine> findByProducerOrderByDeadline(@Param("producer") String producer);
    List<OrderLine> findByShopOrderByCreated(@Param("shop") String shop);

    @Query(value = "select new org.kj6682.gundulf.orders.OrderSynthesis(v.deadline, v.product, SUM(v.quantity)) from OrderLine v where v.producer = ?1 group by (v.deadline, v.product) order by v.deadline asc")
    List<OrderSynthesis> findByProducerGroupByProductOrderByDeadline(@Param("producer") String producer);
}
