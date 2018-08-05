package org.kj6682.gundulf.orders;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
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
import javax.xml.ws.Response;
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


    ResponseEntity<ToDo> post(String product,
                                  Integer size,
                                  LocalDate deadline,
                                  Integer quantity,
                                  String shop) {

        HttpEntity<ToDo> request = new HttpEntity<>(new ToDo(product, size, deadline, quantity));

        ResponseEntity<ToDo> response = restTemplate
                .exchange(todos + "/?shop="+shop, HttpMethod.POST, request, ToDo.class);

        Assert.isTrue(response.getStatusCode().equals(HttpStatus.CREATED)
                || response.getStatusCode().equals(HttpStatus.OK), "HttpStatus must be CREATED or OK");

        ToDo todo = response.getBody();

        Assert.notNull(todo, "todo is not null");
        Assert.isTrue(todo.getProduct().equals(product), "the returned product must be the same");
        Assert.isTrue(todo.getDeadline().isEqual(deadline), "the returned deadline must be the same");

        return response;

    }

    ResponseEntity<ToDo> markToDo(String product,
                                  Integer size,
                                  LocalDate deadline,
                                  Integer quantity,
                                  String shop) {

        HttpEntity<ToDo> request = new HttpEntity<>(new ToDo(product, size, deadline, quantity));

        ResponseEntity<ToDo> response = restTemplate
                .exchange(todos + "/?shop="+shop, HttpMethod.POST, request, ToDo.class);

        Assert.isTrue(response.getStatusCode().equals(HttpStatus.CREATED)
                || response.getStatusCode().equals(HttpStatus.OK), "HttpStatus must be CREATED or OK");

        ToDo todo = response.getBody();

        Assert.notNull(todo, "todo is not null");
        Assert.isTrue(todo.getProduct().equals(product), "the returned product must be the same");
        Assert.isTrue(todo.getDeadline().isEqual(deadline), "the returned deadline must be the same");

        return response;

    }


    @Data
    static class ToDo {

        String product;

        int size;

        @JsonSerialize(using = LocalDateSerializer.class)
        @JsonDeserialize(using = LocalDateDeserializer.class)
        LocalDate deadline;

        Integer quantity;

        ToDo(){}

        ToDo(String product, int size, LocalDate deadline, Integer quantity) {
            this.deadline = deadline;
            this.product = product;
            this.size = size;
            this.quantity = quantity.intValue();
        }

    }//:)
}
