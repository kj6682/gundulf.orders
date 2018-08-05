package org.kj6682.gundulf.todo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kj6682.gundulf.orders.ShopOrderRepository;
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
public class DetailJsonTest {

    @Autowired
    private JacksonTester<Detail> json;


    Detail detail;

    File jsonFile;

    @Before
    public void setup() throws Exception{

        detail = new Detail("shop", 1);
        jsonFile = ResourceUtils.getFile("classpath:one_todo_detail.json");

    }

    @Test
    public void serialise() throws Exception{

        System.out.println(this.json.write(detail));
        assertThat(this.json.write(detail)).isEqualTo(jsonFile);
        assertThat(this.json.write(detail)).hasJsonPathStringValue("@.shop");
        assertThat(this.json.write(detail)).hasJsonPathNumberValue("@.quantity");

    }
    @Test
    public void deserialise() throws Exception {

        String jsonObject = new String(Files.readAllBytes(jsonFile.toPath()));
        Detail newToDo = this.json.parse(jsonObject).getObject();
        assertThat(newToDo.equals(detail));

    }

}
