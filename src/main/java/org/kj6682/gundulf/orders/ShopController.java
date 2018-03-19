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
import org.apache.tomcat.jni.Local;
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
@Api(value = "shop-orders", description = "Shop - Orders API")
@RestController
@RequestMapping("/api")
class ShopController {

    @Autowired
    private OrderLineRepository repository;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Value("${API_PRODUCTS}")
    private String products;

    /**
     * ORDER-003 - the_shop_holder_lists_the_orders
     * <p>
     * as a shop holder
     * I want to list all my orders
     * so that I can track what I asked
     * and possibly validate returns
     */
    @GetMapping("/orders/shop/{shop}")
    List<OrderLine> getShopOrders(@PathVariable String shop) {

        return repository.findByShopOrderByDeadlineAndProductAsc(shop);

    }

    @GetMapping("/orders/shop/{shop}/{date}")
    List<OrderLine> getShopOrders(@PathVariable String shop, @PathVariable String date) {

        LocalDate deadline = LocalDate.parse(date);
        return repository.findByShopAndDeadlineOrderByDeadlineAndProductAsc(shop, deadline);

    }

    /**
     * ORDER-004 - the_shop_holder_list_the_products_to_place_orders
     * <p>
     * as a shop holder
     * I want to list all the products of a producer
     * so I can place an order on it
     * and possibly modify it
     */
    @GetMapping("/orders/shop/{shop}/{producer}/{date}")
    List<OrderLine> getProductsAndOrderLines(@PathVariable String shop,
                                             @PathVariable String producer,
                                             @PathVariable String date) {


        LocalDate deadline = LocalDate.parse(date);
        return repository.findByShopAndProducerAndDeadlineOrderByDeadlineAndProductAsc(shop, producer, deadline);

    }



    @PostMapping(value = "/orders/shop/{shop}")
    ResponseEntity<?> createOrderLine(@PathVariable String shop,
                                                @RequestBody OrderLine order) {
        Assert.notNull(order, "Order can not be empty");

        OrderLine result = repository.save(order);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping(value = "/orders/shop/{shop}/{id}")
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

    @DeleteMapping(value = "/orders/shop/{shop}/{id}")
    void deleteOrderLine(@PathVariable String shop,
                               @PathVariable(required = true) Long id) {
        //TODO check the shop
        repository.delete(id);
    }

}//:)
