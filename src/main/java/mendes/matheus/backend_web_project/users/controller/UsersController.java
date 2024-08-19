package mendes.matheus.backend_web_project.users.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mendes.matheus.backend_web_project.infrastructure.util.ResultError;
import mendes.matheus.backend_web_project.users.dto.UsersRequestDTO;
import mendes.matheus.backend_web_project.users.dto.UsersSimpleResponseDTO;
import mendes.matheus.backend_web_project.users.dto.UsersSummaryResponseDTO;
import mendes.matheus.backend_web_project.users.dto.UsersUpdateDTO;
import mendes.matheus.backend_web_project.users.service.UsersService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UsersController {

    private final UsersService usersService;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createUsers(@Valid @RequestBody List<UsersRequestDTO> bodies, BindingResult result, UriComponentsBuilder uriComponentsBuilder) {

        if (result.hasErrors()) {
            // Se houver erros de validação, retorna status 400 e os erros
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultError.getResultErrors(result));
        }

        List<UsersSimpleResponseDTO> responseList = new ArrayList<>();

        for (UsersRequestDTO body : bodies) {
            // Cria cada usuário
            UsersSimpleResponseDTO usersSimpleResponseDTO = this.usersService.createUser(body);
            responseList.add(usersSimpleResponseDTO);
        }

        // Retorna status 201 Created com a lista de respostas
        return ResponseEntity.status(HttpStatus.CREATED).body(responseList);
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

//    @GetMapping(produces = "application/json")
//    public ResponseEntity<List<UsersSummaryResponseDTO>> getAllUsersDTO() {
//        List<UsersSummaryResponseDTO> users = usersService.getAllUsersDTO();
//        return ResponseEntity.ok(users);
//    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<Page<UsersSummaryResponseDTO>> getAllUsersDTO(
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy) {

        // Cria o PageRequest com offset, pageSize e ordenação
        PageRequest pageRequest = PageRequest.of(offset, pageSize, Sort.by(sortBy));

        // Retorna a página com os dados
        return ResponseEntity.ok(usersService.getAllUsersDTO(pageRequest));
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
