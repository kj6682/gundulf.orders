package org.kj6682.gundulf.orders;

import org.hibernate.criterion.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
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
public class SpecialOrderJsonTest {

    @Autowired
    private JacksonTester<SpecialOrder> json;

    @MockBean
    private SpecialOrderRepository repository;

    SpecialOrder specialOrder;


    File jsonFile;


    @Before
    public void setup() throws Exception{
        OrderLine simpleOrder = new OrderLine("paris",
                                      "four",
                                      "millefeuilles-6",
                                      10,
                                      LocalDate.of(2017,12,3),
                                      LocalDate.of(2017,12,4) );
        specialOrder = new SpecialOrder();
        specialOrder.setDeadline(LocalDate.of(2018,01,31));
        specialOrder.getOrders().add(simpleOrder);

        jsonFile = ResourceUtils.getFile("classpath:one-special-order.json");

    }
    @Test
    public void serialise() throws Exception{
        System.out.println(this.json.write(specialOrder));
        assertThat(this.json.write(specialOrder)).isEqualTo(jsonFile);
    }

    @Test
    public void deserialise() throws Exception {

        String jsonObject = new String(Files.readAllBytes(jsonFile.toPath()));
        SpecialOrder newCake = this.json.parse(jsonObject).getObject();
        assertThat(newCake.equals(specialOrder));
        assertThat(newCake.getOrders().size() == 1);
        assertThat(newCake.getOrders().iterator().next().getProducer() == "four");
    }
}
