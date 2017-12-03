package org.kj6682.gundulf.orders;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by luigi on 02/12/2017.
 */
@Api(value = "orders", description = "Orders API")
@RestController
@RequestMapping("/api")
class Controller {

    @Autowired
    private SimpleOrderRepository repository;

    /**
     *
     * This service should be called by a producer to get the list of orders to be treated
     *
     */
    @GetMapping("/orders/to/{producer}")
    List<SimpleOrder> to(@PathVariable String producer) {


        return repository.findByProducerOrderByDeadline(producer).stream()
                .collect(Collectors.toList());

    }

    /**
     *
     * This service should be called by a shop to track the list of orders
     *
     */
    @GetMapping("/orders/from/{shop}")
    List<SimpleOrder> from(@PathVariable String shop) {

        return repository.findByShopOrderByCreated(shop).stream()
                .collect(Collectors.toList());

    }

    @PostMapping(value = "/products/to/{producer}")
    ResponseEntity<?> create(@PathVariable String producer,
                             @RequestBody SimpleOrder order) {
        Assert.notNull(order, "Order can not be empty");
        //TODO check the producer
        SimpleOrder result = repository.save(order);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/products/from/{shop}/{id}")
    void delete(@PathVariable String shop,
                @PathVariable(required = true) Long id) {
        //TODO check the producer
        repository.delete(id);
    }

}
