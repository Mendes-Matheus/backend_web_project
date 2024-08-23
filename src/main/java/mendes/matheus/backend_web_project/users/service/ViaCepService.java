package mendes.matheus.backend_web_project.users.service;

import lombok.RequiredArgsConstructor;
import mendes.matheus.backend_web_project.users.dto.AddressDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ViaCepService {

    private final WebClient webClient;
    private static final Logger log = LoggerFactory.getLogger(UsersService.class);

    public Mono<AddressDTO> getAddressByCep(String cep) {
        return webClient
                .get()
                .uri(cep + "/json/")
                .retrieve()
                .bodyToMono(AddressDTO.class)
                .doOnNext(address -> log.info("Fetched address: {}", address))
                .doOnError(error -> log.error("Error fetching address", error));
    }
}
