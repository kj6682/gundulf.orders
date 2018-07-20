package org.kj6682.gundulf.orders;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by luigi on 02/12/2017.
 */
@Api(value = "orders", description = "Orders API")
@RestController("OrdersController")
@RequestMapping("/api/orders/v2.0")
class Controller {


    @Autowired
    private ShopOrderRepository shopOrderRepository;


    @Autowired
    private ToDoService toDoService;

    @GetMapping("/")
    List<ShopOrder> getAllOrders() {

        List<ShopOrder> orders = shopOrderRepository.findAll();
        toDoService.list();

        return orders;

    }

    @PostMapping(value = "/")
    ResponseEntity<?> createOrder(@RequestBody ShopOrder shopOrder) {
        Assert.notNull(shopOrder, "ShopOrder can not be empty");

        shopOrder.getProducts().forEach(p ->
                toDoService.create(p.getName(),
                        p.getSize(),
                        shopOrder.getDeadline(),
                        p.getQuantity() ));
        ;

        ShopOrder result = shopOrderRepository.save(shopOrder);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }


    @DeleteMapping(value = "/{id}")
    void deleteOrder(@PathVariable(required = true) Long id) {
        System.out.println("bloody delete " + id);
        Assert.notNull(id, "ShopOrder id can not be null");

        ShopOrder shopOrder = shopOrderRepository.findOne(id);
        Assert.notNull(shopOrder, "The ShopOrder you want to delete must not be null");

        shopOrder.getProducts().forEach(p ->
                toDoService.create(p.getName(),
                        p.getSize(),
                        shopOrder.getDeadline(),
                        p.getQuantity() * -1 ));
        ;

        shopOrderRepository.delete(id);
    }

}//:)
