package org.kj6682.gundulf.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Component
class ApiBouncer {

    @Autowired
    private RestTemplate restTemplate;

    private HttpHeaders requestHeaders;

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(new MediaType("application","json"));
        requestHeaders.setAccept(Collections.singletonList(new MediaType("application","json")));

        builder.messageConverters().additionalMessageConverters(new MappingJackson2HttpMessageConverter(), new StringHttpMessageConverter());
        return builder.build();
    }


    ResponseEntity<String> get(String endpoint) {
        ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);
        Assert.isTrue(HttpStatus.OK.equals(response.getStatusCode()),"something got wrong with " + endpoint);
        return response;
    }

    ResponseEntity<?> post(String endpoint, @RequestBody String body) {
        HttpEntity<?> requestEntity = new HttpEntity<Object>(body, requestHeaders);

        ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.POST, requestEntity, String.class);

        return response;
    }

    void delete(String endpoint) {

        restTemplate.delete(endpoint);
    }
}
