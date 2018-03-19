package org.kj6682.gundulf.orders;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by luigi on 02/12/2017.
 */
@Api(value = "admin-orders", description = "Admin - Orders API")
@RestController
@RequestMapping("/api")
class AdminController {


    @Autowired
    private OrderLineRepository repository;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    /**
     * ORDER - the_admin_lists_all_the_orders
     * <p>
     * as an admin
     * I want to list all the orders
     * so that I can do some maintenance
     */
    @GetMapping("/orders")
    List<OrderLine> findAll() {

        return repository.findAll();

    }


}//:)
