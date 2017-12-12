package org.kj6682.gundulf.orders;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by luigi on 12/07/2017.
 *
 * TDD - use this test to define the ORDER model
 *
 */
@RunWith(SpringRunner.class)
@org.springframework.boot.test.autoconfigure.json.JsonTest
public class ProductLinesJsonTest {

    File jsonOneProduct;
    File jsonManyProducts;
    File jsonManyOrders;

    @Before
    public void setup() throws Exception{
        jsonOneProduct = ResourceUtils.getFile("classpath:one-product.json");
        jsonManyProducts = ResourceUtils.getFile("classpath:many-products.json");
        jsonManyOrders = ResourceUtils.getFile("classpath:many-orders.json");

    }
    @Test
    public void serialise() throws Exception{

        ObjectMapper objectMapper = new ObjectMapper();
        OrderLine order =  new OrderLine();
        order.setId((long)0);
        order.setProduct("millefoglie");
        order.setProducer("alibaba");
        objectMapper.writeValue(new File("target/two.json"), order);

    }

    @Test
    public void deserialiseOne() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        SimpleModule module = new SimpleModule(Controller.ProductDeserializer.class.getName(), new Version(1, 0, 0, null, null, null));
        module.addDeserializer(OrderLine.class, new Controller.ProductDeserializer());
        objectMapper.registerModule(module);

        String jsonProductArray = new String(Files.readAllBytes(jsonOneProduct.toPath()));

        OrderLine one = objectMapper.readValue(jsonProductArray, OrderLine.class);
        assertThat(one.equals("OrderLine{id=46, product='millefoglie', pieces=1, producer='Four'}"));

    }


    @Test
    public void deserialiseMany() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        String jsonProductArray = new String(Files.readAllBytes(jsonManyProducts.toPath()));
        System.out.println(jsonProductArray);


        List<OrderLine> listProds = objectMapper.readValue(jsonProductArray, new TypeReference<List<OrderLine>>(){});
        listProds.stream().forEach(System.out::println);
    }

    @Test
    public void map() throws Exception{

        String jsonProductArray = new String(Files.readAllBytes(jsonOneProduct.toPath()));
        TypeReference<HashMap<String, String>> typeRef
                = new TypeReference<HashMap<String, String>>() {};

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, String> map = objectMapper.readValue(jsonProductArray, typeRef);
        System.out.println(map);
        System.out.println(map.get("name"));

    }

    @Test
    public void list2map() throws Exception{

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        String jsonOrdersArray = new String(Files.readAllBytes(jsonManyOrders.toPath()));


        List<OrderLine> listProds = objectMapper.readValue(jsonOrdersArray, new TypeReference<List<OrderLine>>(){});


        Map<String, OrderLine> result1 = listProds.stream().collect(
                Collectors.toMap(OrderLine::getProduct, o -> o));

        System.out.println(result1);

    }
}
