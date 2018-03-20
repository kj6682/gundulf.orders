package org.kj6682.gundulf.orders;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.kj6682.gundulf.orders.orderline.OrderLine;
import org.kj6682.gundulf.orders.orderline.OrderLineRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Profile({"ddl-create", "h2"})
	@Bean
	CommandLineRunner initItems(OrderLineRepository orderLineRepository) throws IOException {
		org.springframework.core.io.Resource resource = new ClassPathResource("orderlines.json");
		BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
		final String jsonArray = reader.lines().collect(Collectors.joining());
		ObjectMapper mapper = new ObjectMapper();

		OrderLine[] asArray = mapper.readValue(jsonArray, OrderLine[].class);

		return (evt) -> {
			Arrays.asList(asArray).forEach(
					order -> {
						orderLineRepository.save(order);
						System.out.println(order);
					}
			);

		};

	}
}
