package org.kj6682.gundulf.orders;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by luigi on 12/07/2017.
 *
 * TDD - use this test to define the ORDER model
 *
 */
@RunWith(SpringRunner.class)
@JsonTest
public class OrderJsonTest {

    @Autowired
    private JacksonTester<Order> json;

    @MockBean
    private OrderRepository orderRepository;

    Product cake1, cake4;

    Order order;

    File jsonFile;

    @Before
    public void setup() throws Exception{
        cake1 = new Product("product",
                "category",1,1);
        cake4 = new Product("product",
                "category",1,4);

        Set<Product> products = new HashSet<Product>();
        products.add(cake1);
        products.add(cake4);

        order = new Order("customer",
                "address",
                "shop",
                LocalDate.of(2018, 07, 15),
                LocalDate.of(2018, 07, 16),
                products
        );

        jsonFile = ResourceUtils.getFile("classpath:one_order.json");

    }
    @Test
    public void serialise() throws Exception{

        assertThat(this.json.write(order)).isEqualTo(jsonFile);
        assertThat(this.json.write(order)).hasJsonPathStringValue("@.customer");
        assertThat(this.json.write(order)).hasJsonPathStringValue("@.address");
        assertThat(this.json.write(order)).hasJsonPathValue("@.shop");
        assertThat(this.json.write(order)).hasJsonPathValue("@.products");
    }
    @Test
    public void deserialise() throws Exception {

        String jsonObject = new String(Files.readAllBytes(jsonFile.toPath()));
        Order newOrder = this.json.parse(jsonObject).getObject();
        assertThat(newOrder.equals(order));

    }

}
