package org.kj6682.gundulf.orders;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by luigi on 02/12/2017.
 */
@Api(value = "orders", description = "Orders API")
@RestController
@RequestMapping("/api")
class CustomerController {


    @Autowired
    private OrderRepository orderRepository;


    @GetMapping("/orders/customer")
    List<Order> getCustomerOrders() {

        return orderRepository.findAll();

    }

    @PostMapping(value = "/orders/customer")
    ResponseEntity<?> createCustomerOrder(@RequestBody Order order) {
        Assert.notNull(order, "Order can not be empty");

        Order result = orderRepository.save(order);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

}//:)
