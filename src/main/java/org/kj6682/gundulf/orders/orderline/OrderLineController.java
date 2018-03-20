package org.kj6682.gundulf.orders.orderline;

import io.swagger.annotations.Api;
import org.kj6682.gundulf.orders.CustomerOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by luigi on 02/12/2017.
 */
@Api(value = "orderlines", description = "OrderLines API")
@RestController
@RequestMapping("/api/orderlines")
public class OrderLineController {

    @Autowired
    private OrderLineRepository repository;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @GetMapping()
    List<OrderLine> getAllOrderlines() {

        return repository.findAll();

    }

    @GetMapping("/producer/{producer}")
    List<OrderLine> getProducerOrderLines(@PathVariable String producer) {

        return repository.findByProducerOrderByDeadlineAndProductAsc(producer);

    }

    @GetMapping("/shop/{shop}")
    List<OrderLine> getShopOrderLines(@PathVariable String shop) {

        return repository.findByShopOrderByDeadlineAndProductAsc(shop);

    }

    @GetMapping("/shop/{shop}/{date}")
    List<OrderLine> getShopOrderLinesByDate(@PathVariable String shop, @PathVariable String date) {

        LocalDate deadline = LocalDate.parse(date);
        return repository.findByShopAndDeadlineOrderByDeadlineAndProductAsc(shop, deadline);

    }

    @GetMapping("/shop/{shop}/{producer}/{date}")
    List<OrderLine> getShopOrderLinesByProducerAndDate(@PathVariable String shop,
                                             @PathVariable String producer,
                                             @PathVariable String date) {


        LocalDate deadline = LocalDate.parse(date);
        return repository.findByShopAndProducerAndDeadlineOrderByDeadlineAndProductAsc(shop, producer, deadline);

    }



    @PostMapping(value = "/shop/{shop}")
    ResponseEntity<?> createOrderLine(@PathVariable String shop,
                                                @RequestBody OrderLine order) {
        Assert.notNull(order, "Order can not be empty");

        OrderLine result = repository.save(order);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping(value = "/shop/{shop}/{id}")
    ResponseEntity<?> updateOrderLine(@PathVariable String shop,
                                               @PathVariable Long id,
                                               @RequestBody OrderLine order) {
        Assert.notNull(order, "Order can not be empty");
        //TODO check the producer
        OrderLine ol = repository.findOne(id);
        ol.setQuantity(order.getQuantity());
        OrderLine result = repository.save(ol);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping(value = "/shop/{shop}/{id}")
    void deleteOrderLine(@PathVariable String shop,
                               @PathVariable(required = true) Long id) {
        //TODO check the shop
        repository.delete(id);
    }

}//:)
