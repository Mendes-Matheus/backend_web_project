package mendes.matheus.backend_web_project.users.controller;


import lombok.RequiredArgsConstructor;
import mendes.matheus.backend_web_project.users.dto.UsersIdDTO;
import mendes.matheus.backend_web_project.users.dto.UsersRequestDTO;
import mendes.matheus.backend_web_project.users.service.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UsersController {

    private final UsersService usersService;

    @PostMapping
    public ResponseEntity<UsersIdDTO> createUser(@RequestBody UsersRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        UsersIdDTO usersIdDTO = this.usersService.createUser(body);

        var uri = uriComponentsBuilder.path("/user/{id}").buildAndExpand(usersIdDTO.userId()).toUri();

        return ResponseEntity.created(uri).body(usersIdDTO);
    }

}
