package mendes.matheus.backend_web_project.users.service;

import lombok.RequiredArgsConstructor;
import mendes.matheus.backend_web_project.users.exceptions.UserAlreadyExistsException;
import mendes.matheus.backend_web_project.users.exceptions.UserNotFoundException;
import mendes.matheus.backend_web_project.users.model.Users;
import mendes.matheus.backend_web_project.users.dto.UsersIdDTO;
import mendes.matheus.backend_web_project.users.dto.UsersRequestDTO;
import mendes.matheus.backend_web_project.users.repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;


    public UsersIdDTO createUser(@RequestBody @Validated UsersRequestDTO usersRequestDTO) {

        // Verifica se já existe um usuário com o email fornecido
        boolean existsByEmail = this.usersRepository
                .findByEmail(usersRequestDTO.email())
                .isPresent();

        // Verifica se já existe um usuário com o username fornecido
        boolean existsByUsername = this.usersRepository
                .findByUsername(usersRequestDTO.username())
                .isPresent();

        // Usa Optional para encadear verificações e operações
        return Optional.of(usersRequestDTO)
                .filter(req -> !existsByEmail && !existsByUsername)
                .map(req -> {
                    Users newUser = new Users();
                    newUser.setName(req.name());
                    newUser.setEmail(req.email());
                    newUser.setUsername(req.username()); // Certifique-se de definir o username
                    newUser.setPassword(req.password());
                    this.usersRepository.save(newUser);
                    return new UsersIdDTO(newUser.getId());
                })
                .orElseThrow(() -> new UserAlreadyExistsException("User already exists"));
    }

    public Users getUserById(Long userId){
        return this.usersRepository.findUsersById(userId).orElseThrow(()
                -> new UserNotFoundException("User not found with ID: " + userId));
    }

    public Users getUserByEmail(String email){
        return this.usersRepository.findByEmail(email).orElseThrow(()
                -> new UserNotFoundException("User not found with email: " + email));
    }
}
