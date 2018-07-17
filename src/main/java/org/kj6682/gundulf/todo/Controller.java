package org.kj6682.gundulf.todo;

import io.swagger.annotations.Api;
import org.kj6682.gundulf.orders.ShopOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by luigi on 02/12/2017.
 */
@Api(value = "todos", description = "Producer Todos API")
@RestController("ToDoController")
@RequestMapping("/api/todos/v2.0")
public class Controller {


    @Autowired
    private ToDoRepository repository;

    @Autowired
    private ShopOrderRepository shopOrderRepository;


    @GetMapping("/")
    List<ToDo> getProducerTodos() {

        List<ToDo> result = repository.findAll();

        return result;

    }

    @PostMapping(value = "/")
    ResponseEntity<?> create(@RequestBody ToDo todo) {

        ToDo t = repository.findOne(new ToDoKey(todo.getProduct(), todo.getDeadline()));
        if(t != null) {
            todo.setQuantity(t.getQuantity() + todo.getQuantity());
        }
        ToDo result = repository.save(todo);

        return new ResponseEntity<ToDo>(result, HttpStatus.CREATED);

    }


    //@RequestMapping(value = "/producer/{producer}/orders.csv")
    public void listTODOAsCSV(@PathVariable String producer, HttpServletResponse response) throws IOException{
        response.setContentType("text/csv; charset=utf-8");

        List<ToDo> todos = repository.findAll();

        StringBuilder sb = new StringBuilder(csvHeader());
        for(ToDo order : todos){
            sb.append(asCsv(order));
        }
        response.getWriter().print(sb.toString());
    }

    static String csvHeader(){
        return "DEADLINE;PRODUCT;QUANTITY\n";
    }

    private String asCsv(ToDo todo) {
        final StringBuilder sb = new StringBuilder();
        sb.append(todo.deadline).append(';');
        sb.append(todo.product).append(';');
        sb.append(todo.quantity).append(';');
        sb.append("\n");
        return sb.toString();
    }

}//:)
