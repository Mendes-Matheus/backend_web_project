package mendes.matheus.backend_web_project.users.service;

import lombok.RequiredArgsConstructor;
import mendes.matheus.backend_web_project.users.model.Users;
import mendes.matheus.backend_web_project.users.dto.UsersIdDTO;
import mendes.matheus.backend_web_project.users.dto.UsersRequestDTO;
import mendes.matheus.backend_web_project.users.repository.UsersRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;

    public UsersIdDTO createUser(UsersRequestDTO usersRequestDTO){
        Users newUser = new Users();

        newUser.setName(usersRequestDTO.name());
        newUser.setEmail(usersRequestDTO.email());
        newUser.setPassword(usersRequestDTO.password());
        this.usersRepository.save(newUser);

        return new UsersIdDTO(newUser.getId());
    }
}
