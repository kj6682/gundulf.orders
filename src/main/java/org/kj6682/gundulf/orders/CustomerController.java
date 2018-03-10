package org.kj6682.gundulf.orders;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by luigi on 02/12/2017.
 */
@Api(value = "customer-orders", description = "Customer - Orders API")
@RestController
@RequestMapping("/api")
class CustomerController {


    @Autowired
    private CustomerOrderRepository customerOrderRepository;


    @GetMapping("/orders/customer")
    List<CustomerOrder> getCustomerOrders() {

        return customerOrderRepository.findAll();

    }

    @PostMapping(value = "/orders/customer")
    ResponseEntity<?> createCustomerOrder(@RequestBody CustomerOrder order) {
        Assert.notNull(order, "Order can not be empty");

        CustomerOrder result = customerOrderRepository.save(order);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

}//:)
