package org.kj6682.gundulf.orders;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by luigi on 02/12/2017.
 */
@Api(value = "producer-orders", description = "Producer - Orders API")
@RestController
@RequestMapping("/api")
class ProducerController {


    @Autowired
    ApiBouncer apiBouncer;
    @Autowired
    private OrderLineRepository repository;
    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Value("${API_PRODUCTS}")
    private String products;

    /**
     * ORDER-001 - the_producer_lists_the_orders
     * <p>
     * as a producer
     * I want to list all my order lines
     * so that I can facilitate dispatching the products
     */
    @GetMapping("/orders/producer/{producer}")
    List<OrderLine> producerOrders(@PathVariable String producer) {

        return repository.findByProducerOrderByDeadlineAndProductAsc(producer);

    }

    /**
     * ORDER-002 - the_producer_lists_the_daily_todo
     * <p>
     * as a producer
     * I want to get my todolist
     * so that I can facilitate my daily work
     * and possibly anticipate the future productions
     */
    @GetMapping("/orders/producer/{producer}/todo")
    List<ToDo> producerTodos(@PathVariable String producer) {

        List<ToDo> result = repository.findByProducerGroupByProductOrderByDeadline(producer);

        return result;

    }


    //@RequestMapping(value = "/orders/producer/{producer}/orders.csv")
    public void listTODOAsCSV(@PathVariable String producer, HttpServletResponse response) throws IOException{
        response.setContentType("text/csv; charset=utf-8");

        List<ToDo> todos = repository.findByProducerGroupByProductOrderByDeadline(producer);

        StringBuilder sb = new StringBuilder(ToDo.csvHeader());
        for(ToDo order : todos){
            sb.append(order.asCsv());
        }
        response.getWriter().print(sb.toString());
    }

}//:)
