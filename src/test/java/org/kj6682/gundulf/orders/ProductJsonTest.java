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
public class ProductJsonTest {

    @Autowired
    private JacksonTester<Product> json;

    @MockBean
    private ProductRepository productRepository;

    Product cake;

    File jsonFile;

    @Before
    public void setup() throws Exception{
        cake = new Product("product",
                "category",1,1);
        jsonFile = ResourceUtils.getFile("classpath:one_product.json");

    }
    @Test
    public void serialise() throws Exception{

        assertThat(this.json.write(cake)).isEqualTo(jsonFile);
        assertThat(this.json.write(cake)).hasJsonPathStringValue("@.name");
        assertThat(this.json.write(cake)).hasJsonPathStringValue("@.category");
        assertThat(this.json.write(cake)).hasJsonPathValue("@.quantity");
        assertThat(this.json.write(cake)).hasJsonPathValue("@.size");
    }
    @Test
    public void deserialise() throws Exception {

        String jsonObject = new String(Files.readAllBytes(jsonFile.toPath()));
        Product newCake = this.json.parse(jsonObject).getObject();
        assertThat(newCake.equals(cake));

    }

}
