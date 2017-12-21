package org.kj6682.gundulf.orders;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.data.repository.query.Param;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by luigi on 12/07/2017.
 * <p>
 * TDD - use this test to define the ORDER model
 */
@RunWith(SpringRunner.class)
@org.springframework.boot.test.autoconfigure.json.JsonTest
public class MergeProductLinesAndOrdersTest {

    List<OrderLine> orderLines;

    @Autowired
    private JacksonTester<OrderLine> json;

    File jsonManyProducts;
    File jsonManyOrders;
    File jsonOneOrder;

    private String producer = "four";
    private String shop = "paris";
    private LocalDate tomorrow;
    private LocalDate deadline;
    private OrderLine testOrder;

    @Before
    public void setup() throws Exception {

        jsonManyProducts = ResourceUtils.getFile("classpath:many-products.json");
        jsonManyOrders = ResourceUtils.getFile("classpath:many-orders.json");
        jsonOneOrder = ResourceUtils.getFile("classpath:one-order.json");

        DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        deadline = LocalDate.parse("2017-12-04", dateformatter);
        tomorrow = LocalDate.parse(LocalDate.now().plusDays(1L).toString(), dateformatter);

        String jsonObject = new String(Files.readAllBytes(jsonOneOrder.toPath()));
        testOrder = this.json.parse(jsonObject).getObject();
        testOrder.setDeadline(LocalDate.now().plusDays(1L));
        testOrder.setId(2000L);
    }


    private Map<String, OrderLine> getProductLines(String shop, String producer)  {


        SimpleModule module = new SimpleModule(Controller.ProductDeserializer.class.getName(), new Version(1, 0, 0, null, null, null));
        Controller.ProductDeserializer productDeserializer = new Controller.ProductDeserializer();
        productDeserializer.setShop(shop);
        module.addDeserializer(OrderLine.class, productDeserializer);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(module);

        Map<String, OrderLine> result;
        try {
            String jsonProds = new String(Files.readAllBytes(jsonManyProducts.toPath()));


            List<OrderLine> productLines = objectMapper.readValue(jsonProds, new TypeReference<List<OrderLine>>() {
            });

            // ..and create a map lead by the product
            result = productLines.stream()
                    .collect(Collectors
                            .toMap(OrderLine::getDeadLineAndProduct, o -> o));
        }catch (IOException e){
            result = new HashMap<>();
            e.printStackTrace();
        }
        return result;
    }

    private Map<String, OrderLine> getOrderLines(String producer) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Map<String, OrderLine> orders;

        try{
            String jsonOrdersArray = new String(Files.readAllBytes(jsonManyOrders.toPath()));
            List<OrderLine> listOrders = objectMapper.readValue(jsonOrdersArray, new TypeReference<List<OrderLine>>() {});
            orders = listOrders.stream()
                .filter(order -> order.getShop().equals(shop)) //&& order.getDeadline().equals(deadline)
                .collect(Collectors.toMap(OrderLine::getDeadLineAndProduct, o -> o)); // there should be no duplicates in the original query
        }catch(IOException e){
            e.printStackTrace();
            orders = new HashMap<>();
        }
        return orders;
    }


    @Test
    public void merge(){

        Map<String, OrderLine> orderLineMap = getProductLines(shop, producer);

        assertThat(orderLineMap.keySet().size()).isEqualTo(4);

        assertThat(orderLineMap.keySet()).containsOnly(
                tomorrow+"baba-1",
                tomorrow+"baba-2",
                tomorrow+"millefeuilles-4",
                tomorrow+"millefeuilles-6");

        Map<String, OrderLine> shopOrders = getOrderLines(producer);

        assertThat(shopOrders.keySet().size()).isEqualTo(3);
        assertThat(shopOrders.keySet()).containsOnly(
                "2017-12-04baba-1",
                "2017-12-04baba-2",
                "2017-12-08baba-2");

        shopOrders.put(tomorrow + "millefeuilles-6", testOrder);

        assertThat(shopOrders.keySet().size()).isEqualTo(4);
        assertThat(shopOrders.keySet()).containsOnly(
                "2017-12-04baba-1",
                "2017-12-04baba-2",
                "2017-12-08baba-2",
                tomorrow+"millefeuilles-6");

        orderLineMap.putAll(shopOrders);

        List<OrderLine> result = orderLineMap.values().stream().collect(Collectors.toList());

        result.sort((l, r) -> {
            if(l.getDeadline().equals(r.getDeadline())){
                return l.getProduct().compareTo(r.getProduct());
            }
            return l.getDeadline().compareTo(r.getDeadline());
        });

        assertThat(result.size()).isEqualTo(7);
        result.forEach(System.out::println);
    }
}
