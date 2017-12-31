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
@Api(value = "orders", description = "Orders API")
@RestController
@RequestMapping("/api")
class Controller {


    @Autowired
    ApiBouncer apiBouncer;
    @Autowired
    private OrderLineRepository repository;
    @Value("${API_PRODUCTS}")
    private String products;

    /**
     * ORDER - the_admin_lists_all_the_orders
     * <p>
     * as an admin
     * I want to list all the orders
     * so that I can do some maintenance
     */
    @GetMapping("/orders")
    List<OrderLine> listAll() {

        return repository.findAll();

    }

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
    List<OrderSynthesis> producerTodos(@PathVariable String producer) {

        List<OrderSynthesis> result = repository.findByProducerGroupByProductOrderByDeadline(producer);

        return result;

    }

    /**
     * ORDER-003 - the_shop_holder_lists_the_orders
     * <p>
     * as a shop holder
     * I want to list all my orders
     * so that I can track what I asked
     * and possibly validate returns
     */
    @GetMapping("/orders/shop/{shop}")
    List<OrderLine> shopOrders(@PathVariable String shop) {

        return repository.findByShopOrderByDeadlineAndProductAsc(shop);

    }

    /**
     * ORDER-004 - the_shop_holder_list_the_products_to_place_orders
     * <p>
     * as a shop holder
     * I want to list all the products of a producer
     * so I can place an order on it
     * and possibly modify it
     */
    @GetMapping("/orders/shop/{shop}/products/{producer}")
    List<OrderLine> dailyOrders(@PathVariable String shop,
                                        @PathVariable String producer) {


        Map<String, OrderLine> orderLineMap = getProductLines(shop, producer);

        Map<String, OrderLine> shopOrders = getOrderLines(shop, producer);

        orderLineMap.putAll(shopOrders);

        List<OrderLine> result = orderLineMap.values().stream().collect(Collectors.toList());

        result.sort((l, r) -> {
            if(l.getDeadline().equals(r.getDeadline())){
                return l.getProduct().compareTo(r.getProduct());
            }
            return l.getDeadline().compareTo(r.getDeadline());
        });
        return result;
    }

    private Map<String, OrderLine> getOrderLines(@PathVariable String shop, @PathVariable String producer) {

        LocalDate deadline = LocalDate.now().plusDays(1);

        return repository.findByShopAndProducerAndDeadlineOrderByDeadlineAndProductAsc(shop, producer, deadline).stream()
                .collect(Collectors
                        .toMap(OrderLine::getDeadLineAndProduct, o -> o));
    }

    /**
     * get the list of products and transform them into orderlines
     *
     * @param producer
     * @return the list of order lines created with the product list
     * @throws IOException
     */
    private Map<String, OrderLine> getProductLines(String shop, String producer) {

        SimpleModule module = new SimpleModule(ProductDeserializer.class.getName(), new Version(1, 0, 0, null, null, null));
        Controller.ProductDeserializer productDeserializer = new Controller.ProductDeserializer();
        productDeserializer.setShop(shop);
        module.addDeserializer(OrderLine.class, productDeserializer);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(module);

        String jsonProds = apiBouncer.get(products + "/" + producer).getBody();

        Map<String, OrderLine> result;
        try{
            List<OrderLine> productLines = objectMapper.readValue(jsonProds, new TypeReference<List<OrderLine>>() {});

            result = productLines.stream()
                .collect(Collectors
                        .toMap(OrderLine::getDeadLineAndProduct, o -> o));

        }catch (IOException e){
            result = new HashMap<>();
            e.printStackTrace();
        }

        return result;
    }


    @PostMapping(value = "/orders/shop/{shop}")
    ResponseEntity<?> create(@PathVariable String shop,
                             @RequestBody OrderLine order) {
        Assert.notNull(order, "Order can not be empty");

        OrderLine result = repository.save(order);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping(value = "/orders/shop/{shop}/{id}")
    ResponseEntity<?> update(@PathVariable String shop,
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
    void delete(@PathVariable String shop,
                @PathVariable(required = true) Long id) {
        //TODO check the shop
        repository.delete(id);
    }

    static class ProductDeserializer extends StdDeserializer<OrderLine> {

        String shop;

        public ProductDeserializer() {
            this(null);
        }

        public ProductDeserializer(Class<?> vc) {
            super(vc);
        }

        void setShop(String shop) {
            this.shop = shop;
        }

        @Override
        public OrderLine deserialize(JsonParser parser, DeserializationContext deserializer) throws IOException {
            OrderLine order = new OrderLine();
            ObjectCodec codec = parser.getCodec();
            JsonNode node = codec.readTree(parser);

            // try catch block
            JsonNode jId = node.get("id");
            Long id = jId.asLong();
            order.setId(0L);

            JsonNode jProduct = node.get("name");
            String product = jProduct.asText();

            JsonNode jPieces = node.get("pieces");
            Integer pieces = jPieces.asInt();
            order.setProduct(product + "-" + pieces);

            JsonNode jProducer = node.get("producer");
            String producer = jProducer.asText();
            order.setProducer(producer);

            order.setShop(shop);
            order.setQuantity(0);
            order.setStatus("NEW");
            order.setCreated(LocalDate.now());
            order.setDeadline(LocalDate.now().plusDays(1L));
            return order;
        }
    }//:)

}//:)
