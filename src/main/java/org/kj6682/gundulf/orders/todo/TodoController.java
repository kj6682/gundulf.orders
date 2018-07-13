package org.kj6682.gundulf.orders.todo;

import io.swagger.annotations.Api;
import org.kj6682.gundulf.orders.OrderRepository;
import org.kj6682.gundulf.orders.orderline.OrderLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by luigi on 02/12/2017.
 */
@Api(value = "todos", description = "Producer Todos API")
@RestController
@RequestMapping("/api/todos")
public class TodoController {


    @Autowired
    private OrderLineRepository repository;

    @Autowired
    private OrderRepository orderRepository;


    @GetMapping("/producer/{producer}")
    List<ToDo> getProducerTodos(@PathVariable String producer) {

        List<ToDo> result = repository.findByProducerGroupByProductOrderByDeadline(producer);

        return result;

    }


    //@RequestMapping(value = "/producer/{producer}/orders.csv")
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
