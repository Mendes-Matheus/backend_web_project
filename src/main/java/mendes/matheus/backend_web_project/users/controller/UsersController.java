package mendes.matheus.backend_web_project.users.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mendes.matheus.backend_web_project.infrastructure.util.ResultError;
import mendes.matheus.backend_web_project.users.dto.UsersRequestDTO;
import mendes.matheus.backend_web_project.users.dto.UsersSimpleResponseDTO;
import mendes.matheus.backend_web_project.users.dto.UsersSummaryResponseDTO;
import mendes.matheus.backend_web_project.users.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UsersController {

    private final UsersService usersService;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createUser(@Valid @RequestBody UsersRequestDTO body, BindingResult result, UriComponentsBuilder uriComponentsBuilder) {

        if (result.hasErrors()) {
            // Se houver erros de validação, retorna status 400 e os erros
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultError.getResultErrors(result));
        }

        // Se não houver erros, cria o usuário e constrói a URI
        UsersSimpleResponseDTO usersSimpleResponseDTO = this.usersService.createUser(body);
        var uri = uriComponentsBuilder.path("/user/{id}").buildAndExpand(usersSimpleResponseDTO.username()).toUri();

        // Retorna status 201 Created com a URI e o corpo da resposta contendo usersSimpleResponseDTO
        return ResponseEntity.created(uri).body(usersSimpleResponseDTO);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<UsersSummaryResponseDTO>> getAllUsersDTO() {
        List<UsersSummaryResponseDTO> users = usersService.getAllUsersDTO();
        return ResponseEntity.ok(users);
    }


}
