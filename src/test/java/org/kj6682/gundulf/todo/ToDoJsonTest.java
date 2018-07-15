package org.kj6682.gundulf.todo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kj6682.gundulf.orders.Product;
import org.kj6682.gundulf.orders.ShopOrder;
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
public class ToDoJsonTest {

    @Autowired
    private JacksonTester<ToDo> json;

    @MockBean
    private ShopOrderRepository shopOrderRepository;

    ToDo toDo;

    File jsonFile;

    @Before
    public void setup() throws Exception{

        toDo = new ToDo("product", LocalDate.of(2018,07,16), 1);

        jsonFile = ResourceUtils.getFile("classpath:one_todo.json");

    }

    @Test
    public void serialise() throws Exception{

        System.out.println(this.json.write(toDo));
        assertThat(this.json.write(toDo)).isEqualTo(jsonFile);
        assertThat(this.json.write(toDo)).hasJsonPathStringValue("@.product");
        assertThat(this.json.write(toDo)).hasJsonPathStringValue("@.deadline");

    }
    @Test
    public void deserialise() throws Exception {

        String jsonObject = new String(Files.readAllBytes(jsonFile.toPath()));
        ToDo newToDo = this.json.parse(jsonObject).getObject();
        assertThat(newToDo.equals(toDo));

    }

}
