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
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
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
public class ProductLinesJsonTest {

    File jsonOneProduct;
    File jsonManyProducts;
    File jsonOneOrder;
    File jsonManyOrders;
    ObjectMapper objectMapper;

    @Before
    public void setup() throws Exception {
        jsonOneProduct = ResourceUtils.getFile("classpath:one-product.json");
        jsonManyProducts = ResourceUtils.getFile("classpath:many-products.json");
        jsonOneOrder = ResourceUtils.getFile("classpath:one-order.json");
        jsonManyOrders = ResourceUtils.getFile("classpath:many-orders.json");

        objectMapper = getObjectMapper();

    }

    @Test
    public void serialise() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        OrderLine order = new OrderLine();
        order.setId((long) 0);
        order.setProduct("millefeuilles");
        order.setProducer("alibaba");
        order.setDeadline(LocalDate.now());
        objectMapper.writeValue(new File("target/two.json"), order);

    }

    @Test
    public void deserialiseOne() throws Exception {

        SimpleModule module = new SimpleModule(ShopController.ProductDeserializer.class.getName(), new Version(1, 0, 0, null, null, null));
        module.addDeserializer(OrderLine.class, new ShopController.ProductDeserializer());

        objectMapper.registerModule(module);

        String jsonProductArray = new String(Files.readAllBytes(jsonOneProduct.toPath()));

        OrderLine one = objectMapper.readValue(jsonProductArray, OrderLine.class);
        assertThat(one.getId()).isEqualTo(0L);
        assertThat(one.getProduct()).isEqualTo("millefeuilles-1");
        assertThat(one.getProducer()).isEqualTo("four");
        assertThat(one.getQuantity()).isEqualTo(0);
        assertThat(one.getStatus()).isEqualTo("NEW");


    }

    private ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }


    @Test
    public void deserialiseManyProductsAndTransformThemIntoOrderLines() throws Exception {

        SimpleModule module = new SimpleModule(ShopController.ProductDeserializer.class.getName(), new Version(1, 0, 0, null, null, null));
        ShopController.ProductDeserializer productDeserializer = new ShopController.ProductDeserializer();
        productDeserializer.setShop("Paris");
        module.addDeserializer(OrderLine.class, productDeserializer);

        objectMapper.registerModule(module);

        String jsonProductArray = new String(Files.readAllBytes(jsonManyProducts.toPath()));

        List<OrderLine> listProds = objectMapper.readValue(jsonProductArray, new TypeReference<List<OrderLine>>() {});
        assertThat(listProds).hasSize(4);
        assertThat(listProds.get(0)).isInstanceOf(OrderLine.class);

    }


}
