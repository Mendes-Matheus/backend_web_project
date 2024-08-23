package mendes.matheus.backend_web_project;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;


@SpringBootApplication
public class BackendWebProjectApplication {

	@Value("${viacep_api_url}")
	private String viaCepApiUrl;

	@Bean
	public WebClient webClient(WebClient.Builder builder) {
		return builder
				.baseUrl(viaCepApiUrl)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(BackendWebProjectApplication.class, args);
	}

}
