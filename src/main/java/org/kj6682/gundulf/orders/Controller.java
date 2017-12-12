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
import java.util.List;
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
     * ORDER-001 - the_producer_lists_the_orders
     * <p>
     * as a producer
     * I want to list all my order lines
     * so that I can facilitate dispatching the products
     */
    @GetMapping("/orders/to/{producer}")
    List<OrderLine> producerOrders(@PathVariable String producer) {

        return repository.findByProducerOrderByDeadline(producer).stream()
                .collect(Collectors.toList());

    }

    /**
     * ORDER-002 - the_producer_lists_the_daily_todo
     * <p>
     * as a producer
     * I want to get my todolist
     * so that I can facilitate my daily work
     * and possibly anticipate the future productions
     */
    @GetMapping("/orders/to/{producer}/group_by_product")
    List<OrderLine> producerTodos(@PathVariable String producer) {

        return repository.findByProducerOrderByDeadline(producer).stream()
                .collect(Collectors.toList());

    }

    /**
     * ORDER-003 - the_shop_holder_lists_the_orders
     * <p>
     * as a shop holder
     * I want to list all my orders
     * so that I can track what I asked
     * and possibly validate returns
     */
    @GetMapping("/orders/from/{shop}")
    List<OrderLine> shopOrders(@PathVariable String shop) {

        return repository.findByShopOrderByCreated(shop).stream()
                .collect(Collectors.toList());

    }

    /**
     * ORDER-004 - the_shop_holder_list_the_products_to_place_orders
     * <p>
     * as a shop holder
     * I want to list all the products of a producer
     * so I can place an order on it
     * and possibly modify it
     */
    @GetMapping("/orders/from/{shop}/to/{producer}")
    List<OrderLine> shopCatalogAndCaddy(@PathVariable String shop,
                                        @PathVariable String producer) {


        List<OrderLine> shopOrders = repository.findByProducerOrderByDeadline(producer).stream()
                .collect(Collectors.toList());

        try {

            shopOrders.addAll(getProductLines(producer));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return shopOrders;
    }

    /**
     * get the list of products and transform them into orderlines
     *
     * @param producer
     * @return the list of order lines created with the product list
     * @throws IOException
     */
    private List<OrderLine> getProductLines(@PathVariable String producer) throws IOException {

        SimpleModule module = new SimpleModule(ProductDeserializer.class.getName(), new Version(1, 0, 0, null, null, null));
        module.addDeserializer(OrderLine.class, new ProductDeserializer());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(module);

        String jsonProds = apiBouncer.get(products + "/" + producer).getBody();
        return objectMapper.readValue(jsonProds, new TypeReference<List<OrderLine>>() {
        });
    }


    @PostMapping(value = "/orders/to/{producer}")
    ResponseEntity<?> create(@PathVariable String producer,
                             @RequestBody OrderLine order) {
        Assert.notNull(order, "Order can not be empty");
        //TODO check the producer
        OrderLine result = repository.save(order);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/orders/from/{shop}/{id}")
    void delete(@PathVariable String shop,
                @PathVariable(required = true) Long id) {
        //TODO check the shop
        repository.delete(id);
    }

    static class ProductDeserializer extends StdDeserializer<OrderLine> {

        public ProductDeserializer() {
            this(null);
        }

        public ProductDeserializer(Class<?> vc) {
            super(vc);
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

            return order;
        }
    }//:)

}//:)
