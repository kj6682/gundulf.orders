package org.kj6682.gundulf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/*@Profile({"ddl-create", "h2"})
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
	*/
}
