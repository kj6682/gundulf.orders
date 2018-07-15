package org.kj6682.gundulf.orders;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.kj6682.commons.LocalDateDeserializer;
import org.kj6682.commons.LocalDateSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import java.net.URI;
import java.time.LocalDate;
import java.util.Collections;

@Service
public class ToDoService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${API_TODO}")
    private String todos;

    private HttpHeaders requestHeaders;

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(new MediaType("application","json"));
        requestHeaders.setAccept(Collections.singletonList(new MediaType("application","json")));

        builder.messageConverters().additionalMessageConverters(new MappingJackson2HttpMessageConverter(), new StringHttpMessageConverter());
        return builder.build();
    }


    ResponseEntity<String> list() {
        String endpoint = todos + "/";
        ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);
        Assert.isTrue(HttpStatus.OK.equals(response.getStatusCode()),
                "something got wrong with " + endpoint);
        return response;
    }


    URI create(String product,
                  LocalDate deadline,
                  Integer quantity) {

        System.out.println(product);
        System.out.println(deadline);
        System.out.println(quantity);

        HttpEntity<ToDo> request = new HttpEntity<>(new ToDo(product, deadline, quantity));
        URI location = restTemplate
                .postForLocation(todos + "/", request);
        //Assert.notNull(location, "not null");

        return  location;


    }


    class ToDo {

        @JsonSerialize
        String product;

        @JsonSerialize(using = LocalDateSerializer.class)
        @JsonDeserialize(using = LocalDateDeserializer.class)
        LocalDate deadline;

        @JsonSerialize
        Integer quantity;

        ToDo(String product, LocalDate deadline, Integer quantity) {
            this.deadline = deadline;
            this.product = product;
            this.quantity = quantity.intValue();
        }

    }//:)
}
