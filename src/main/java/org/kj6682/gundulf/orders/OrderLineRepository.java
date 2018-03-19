package org.kj6682.gundulf.orders;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OrderLineRepository extends CrudRepository<OrderLine, Long> {

    List<OrderLine> findAll();

    @Query(value = "select v " +
            "from OrderLine v " +
            "where v.producer = ?1 " +
            "order by (v.deadline, v.product) asc")
    List<OrderLine> findByProducerOrderByDeadlineAndProductAsc(@Param("producer") String producer);


    @Query(value = "select v " +
            "from OrderLine v " +
            "where v.shop =?1 and and v.deadline = ?2 " +
            "order by (v.deadline, v.product) asc")
    List<OrderLine> findByShopAndDeadlineOrderByDeadlineAndProductAsc(@Param("shop") String shop,
                                                                      @Param("deadline") LocalDate deadline);

    @Query(value = "select v " +
            "from OrderLine v " +
            "where v.shop =?1 and v.producer = ?2 and v.deadline = ?3 " +
            "order by (v.deadline, v.product) asc")
    List<OrderLine> findByShopAndProducerAndDeadlineOrderByDeadlineAndProductAsc(@Param("shop") String shop,
                                                                                 @Param("producer") String producer,
                                                                                 @Param("deadline") LocalDate deadline);


    @Query(value = "select v " +
            "from OrderLine v " +
            "where v.shop = ?1 " +
            "order by (v.deadline, v.product) asc")
    List<OrderLine> findByShopOrderByDeadlineAndProductAsc(@Param("shop") String shop);


    @Query(value = "select " +
            "new org.kj6682.gundulf.orders.ToDo(v.deadline, v.product, SUM(v.quantity)) " +
            "from OrderLine v " +
            "where v.producer = ?1 " +
            "group by (v.deadline, v.product) " +
            "order by (v.deadline, v.product) asc")
    List<ToDo> findByProducerGroupByProductOrderByDeadline(@Param("producer") String producer);
}
