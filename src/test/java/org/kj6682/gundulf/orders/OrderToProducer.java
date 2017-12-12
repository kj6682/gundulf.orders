package org.kj6682.gundulf.orders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kj6682.commons.LocalDateDeserializer;
import org.kj6682.commons.LocalDateSerializer;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by luigi on 12/07/2017.
 * <p>
 * TDD - use this test to define the ORDER model
 */
@RunWith(SpringRunner.class)
@JsonTest
public class OrderToProducer {

    File jsonMany;

    @Before
    public void setup() throws Exception {
        jsonMany = ResourceUtils.getFile("classpath:many-orders.json");
    }



    public void groupByQuantity() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        String jsonProductArray = new String(Files.readAllBytes(jsonMany.toPath()));
        System.out.println(jsonProductArray);

        List<OrderLine> listProds = objectMapper.readValue(jsonProductArray, new TypeReference<List<OrderLine>>() {
        });
        listProds.stream().forEach(System.out::println);

        Map<String, Integer> result = listProds
                .stream()
                .collect(groupingBy(
                        o -> o.getProduct(),
                        summingInt(OrderLine::getQuantity)));

        assertThat(result.get("Baba1").equals(10));
        assertThat(result.get("Baba2").equals(20));
    }

    @Test
    public void orders2producer() throws Exception {

        //given orders(date, producer, product, pieces, quantity)

        //when api/order/to/{producer}
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        String jsonProductArray = new String(Files.readAllBytes(jsonMany.toPath()));

        List<OrderLine> listProds = objectMapper.readValue(jsonProductArray, new TypeReference<List<OrderLine>>() {});

        //then return orders(date, producer, product, sum(quantity)

        Map<String, Integer> niceTry2 =
                listProds.stream()
                        .collect(
                                groupingBy(o -> o.getDeadline()  + "," + o.getProduct(),
                                        summingInt(OrderLine::getQuantity))
                        );

        niceTry2.forEach((k,v) -> {
            String chunks[] = k.split(",");

            OrderSynthesis syn = new OrderSynthesis();

            syn.setProduct(chunks[1]);

            syn.setDeadline(LocalDate.parse(chunks[0], DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            syn.setQuantity(v);

            System.out.println(syn);
        });
        System.out.println(niceTry2);

        List<OrderSynthesis> result = niceTry2.keySet().stream().map( k -> {
                    String chunks[] = k.split(",");

                    OrderSynthesis syn = new OrderSynthesis();

                    syn.setProduct(chunks[1]);

                    syn.setDeadline(LocalDate.parse(chunks[0], DateTimeFormatter.ofPattern("yyyy-MM-dd")));

                    syn.setQuantity(niceTry2.get(k));

                    return syn;
                }
        ).collect(toList());
        String serialized = new ObjectMapper().writeValueAsString(result);
        System.out.println(serialized);

        assertThat(serialized.equals("[{\"deadline\":\"2017-12-04\",\"product\":\"Baba1\",\"quantity\":10},{\"deadline\":\"2017-12-04\",\"product\":\"Baba2\",\"quantity\":20},{\"deadline\":\"2017-12-08\",\"product\":\"Baba2\",\"quantity\":25}]"));

    }

    static class OrderSynthesis{

        @JsonSerialize(using = LocalDateSerializer.class)
        @JsonDeserialize(using = LocalDateDeserializer.class)
        LocalDate deadline;

        String product;

        Integer quantity;

        public OrderSynthesis() {
        }

        public LocalDate getDeadline() {
            return deadline;
        }

        public void setDeadline(LocalDate deadline) {
            this.deadline = deadline;
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("OrderSynthesis{");
            sb.append("deadline=").append(deadline);
            sb.append(", product='").append(product).append('\'');
            sb.append(", quantity=").append(quantity);
            sb.append('}');
            return sb.toString();
        }


    }//:)


}//:)


