package mendes.matheus.backend_web_project.users.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mendes.matheus.backend_web_project.infrastructure.util.ResultError;
import mendes.matheus.backend_web_project.users.dto.*;
import mendes.matheus.backend_web_project.users.service.UsersService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UsersController {

    private final UsersService usersService;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public Mono<ResponseEntity<?>> createUsers(
            @Valid @RequestBody List<UsersRequestDTO> bodies,
            BindingResult result) {

        if (result.hasErrors()) {
            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResultError.getResultErrors(result)));
        }

        // Converte a lista em um Flux reativo
        Flux<UsersSimpleResponseDTO> responseFlux = Flux.fromIterable(bodies)
                .flatMap(usersService::createUser);

        // Coleta o Flux em uma lista para retornar no corpo da resposta
        return responseFlux.collectList()
                .map(responseList -> ResponseEntity.status(HttpStatus.CREATED).body(responseList));
    }


    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<UsersSimpleResponseDTO> getUserById(@PathVariable Long id) {
        UsersSimpleResponseDTO user = this.usersService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping(value = "/username/{username}", produces = "application/json")
    public ResponseEntity<UsersSimpleResponseDTO> getUserByUsername(@PathVariable String username) {
        UsersSimpleResponseDTO user = this.usersService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping(value = "/email/{email}", produces = "application/json")
    public ResponseEntity<UsersSimpleResponseDTO> getUserByEmail(@PathVariable String email) {
        UsersSimpleResponseDTO user = this.usersService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping(value = "/all-users", produces = "application/json")
    public ResponseEntity<List<UsersResponseClassDTO>> getAllUsersDTO() {
        List<UsersResponseClassDTO> users = usersService.getAllUsersDTO();
        return ResponseEntity.ok(users);
    }

    @GetMapping(produces = "application/json")
    public Map<String, Object> getAllUsers(Pageable pageable) {
        // Recupera uma página de objetos UsersResponseClassDTO, com suporte à paginação.
        Page<UsersResponseClassDTO> page = usersService.getAllUsersDTOPageable(pageable);
        List<UsersResponseDTO> dtoList = page.getContent().stream()
                .map(user -> new UsersResponseDTO(user.getId(), user.getName(), user.getUsername(), user.getCep(), user.getCity(), user.getState(), user.getStreet()))
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("users", dtoList);
        response.put("currentPage", page.getNumber());
        response.put("totalItems", page.getTotalElements());
        response.put("totalPages", page.getTotalPages());

        return response;
    }

    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UsersUpdateDTO body, BindingResult result) {
        if (result.hasErrors()) {
            // Se houver erros de validação, retorna status 400 e os erros
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultError.getResultErrors(result));
        }

        // Se não houver erros, atualiza o usuário
        UsersSimpleResponseDTO usersSimpleResponseDTO = this.usersService.updateUser(id, body);

        // Retorna status 200 OK com o corpo da resposta contendo usersSimpleResponseDTO
        return ResponseEntity.ok(usersSimpleResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        this.usersService.deleteUser(id);
        // Retorna status 204 No Content
        return ResponseEntity.noContent().build();
    }


}
