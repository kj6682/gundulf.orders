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
public class SimpleJsonTest {

    @Autowired
    private JacksonTester<SimpleOrder> json;

    @MockBean
    private SimpleOrderRepository repository;

    SimpleOrder simpleOrder;

    File jsonFile;

    @Before
    public void setup() throws Exception{
        simpleOrder = new SimpleOrder("Paris",
                                      "Four",
                                      "Baba",
                                      (short)1,
                                      (short)10,
                                      LocalDate.of(2017,12,3),
                                      LocalDate.of(2017,12,4) );
        jsonFile = ResourceUtils.getFile("classpath:simple.json");

    }
    @Test
    public void serialise() throws Exception{
        System.out.println(this.json.write(simpleOrder));
        assertThat(this.json.write(simpleOrder)).isEqualTo(jsonFile);
    }
    @Test
    public void deserialise() throws Exception {

        String jsonObject = new String(Files.readAllBytes(jsonFile.toPath()));
        SimpleOrder newCake = this.json.parse(jsonObject).getObject();
        assertThat(newCake.equals(simpleOrder));

    }

}
