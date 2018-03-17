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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by luigi on 12/07/2017.
 *
 * TDD - use this test to define the ORDER model
 *
 */
@RunWith(SpringRunner.class)
@JsonTest
public class CustomerOrderJsonTest {

    @Autowired
    private JacksonTester<CustomerOrder> json;

    @MockBean
    private CustomerOrderRepository repository;

    CustomerOrder customerOrder;


    File jsonFile;


    @Before
    public void setup() throws Exception{
        OrderLine simpleOrder = new OrderLine("paris",
                                      "four",
                                      "millefeuilles-6",
                                      10,
                                      LocalDate.of(2017,12,3),
                                      LocalDate.of(2017,12,4) );
        OrderLine simpleOrder2 = new OrderLine("luxembourg",
                "tartes",
                "tiramisu-2",
                10,
                LocalDate.of(2017,12,3),
                LocalDate.of(2017,12,4) );
        customerOrder = new CustomerOrder();
        customerOrder.setCustomer("ricky rat");
        customerOrder.setAddress("23, piazza quattro formaggi, ratcity");
        customerOrder.setCreated(LocalDate.of(2018,01,31));
        customerOrder.setDeadline(LocalDate.of(2018,02,28));
        customerOrder.getOrders().add(simpleOrder);
        customerOrder.getOrders().add(simpleOrder2);

        jsonFile = ResourceUtils.getFile("classpath:one-customer-order.json");

    }
    @Test
    public void serialise() throws Exception{
        System.out.println(this.json.write(customerOrder));
        assertThat(this.json.write(customerOrder)).isEqualTo(jsonFile);
    }

    @Test
    public void deserialise() throws Exception {

        String jsonObject = new String(Files.readAllBytes(jsonFile.toPath()));
        CustomerOrder newCake = this.json.parse(jsonObject).getObject();
        assertThat(newCake.equals(customerOrder));
        assertThat(newCake.getOrders().size() == 1);
        assertThat(newCake.getOrders().iterator().next().getProducer() == "four");
    }
}
