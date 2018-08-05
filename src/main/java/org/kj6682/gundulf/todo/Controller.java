package org.kj6682.gundulf.todo;

import io.swagger.annotations.Api;
import org.kj6682.gundulf.orders.ShopOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

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
    List<ToDo> getAllTodos() {

        List<ToDo> result = repository.findAll();

        return result;

    }

    @GetMapping("/byDate/{date}")
    List<ToDo> getTodosByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<ToDo> result = repository.findByDeadline(date);

        return result;

    }

    @GetMapping("/byProduct/{product}")
    List<ToDo> getTodosByProduct(@PathVariable String product) {

        List<ToDo> result = repository.findByProduct(product);

        return result;

    }

    @PostMapping(value = "/")
    ResponseEntity<?> markTodo(
            @RequestParam(value = "shop", defaultValue = "online", required = false) String shop,
            @RequestBody ToDo data) {

        ToDo todo = repository.findByProductAndSizeAndDeadline(data.getProduct(),
                                                            data.getSize(),
                                                            data.getDeadline());
        if(todo != null) {
            return update(todo, data.getQuantity(), shop);
        }

        return create(data, shop);
    }

    private ResponseEntity<?> create(ToDo todo, String shop){

        todo.addDetail(new Detail(shop, todo.getQuantity()));

        ToDo newTodo = repository.save(todo);

        return new ResponseEntity<ToDo>(newTodo, HttpStatus.CREATED);
    }

    private ResponseEntity<?> update(ToDo todo, Integer quantity, String shop){

        todo.update(shop, quantity);

        ToDo result = repository.save(todo);

        return new ResponseEntity<ToDo>(result, HttpStatus.OK);

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
