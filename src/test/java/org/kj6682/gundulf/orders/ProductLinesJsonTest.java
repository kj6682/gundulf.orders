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
import java.util.List;

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

    File jsonOne;
    File jsonMany;

    @Before
    public void setup() throws Exception{
        jsonOne = ResourceUtils.getFile("classpath:one-product.json");
        jsonMany = ResourceUtils.getFile("classpath:many-products.json");
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

        String jsonProductArray = new String(Files.readAllBytes(jsonOne.toPath()));

        OrderLine one = objectMapper.readValue(jsonProductArray, OrderLine.class);
        assertThat(one.equals("OrderLine{id=46, product='millefoglie', pieces=1, producer='Four'}"));

    }


    @Test
    public void deserialiseMany() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        String jsonProductArray = new String(Files.readAllBytes(jsonMany.toPath()));
        System.out.println(jsonProductArray);


        List<OrderLine> listProds = objectMapper.readValue(jsonProductArray, new TypeReference<List<OrderLine>>(){});
        listProds.stream().forEach(System.out::println);
    }
}
