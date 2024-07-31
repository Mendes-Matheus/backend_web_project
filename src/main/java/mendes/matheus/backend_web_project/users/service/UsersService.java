package mendes.matheus.backend_web_project.users.service;

import lombok.RequiredArgsConstructor;
import mendes.matheus.backend_web_project.infrastructure.mapper.ObjectMapperUtil;
import mendes.matheus.backend_web_project.users.dto.UsersSimpleResponseDTO;
import mendes.matheus.backend_web_project.users.dto.UsersSummaryResponseDTO;
import mendes.matheus.backend_web_project.users.dto.UsersUpdateDTO;
import mendes.matheus.backend_web_project.users.exceptions.UserAlreadyExistsException;
import mendes.matheus.backend_web_project.users.exceptions.UserNotFoundException;
import mendes.matheus.backend_web_project.users.model.Users;
import mendes.matheus.backend_web_project.users.dto.UsersRequestDTO;
import mendes.matheus.backend_web_project.users.repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Matheus Mendes
 */
@Service
@Validated
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final ObjectMapperUtil objectMapperUtil;


    /**
     * Método responsável por criar um usuário
     *
     * @param usersRequestDTO
     * @return
     * retorna um objeto UsersIdDTO com o ID do usuário criado
     */
    public UsersSimpleResponseDTO createUser(@RequestBody @Validated UsersRequestDTO usersRequestDTO) {

        // Verifica se já existe um usuário com o email ou com o username fornecido
        validateUserDoesNotExist(usersRequestDTO);

        Users users = this.usersRepository.save(objectMapperUtil.map(usersRequestDTO, Users.class));
        return new UsersSimpleResponseDTO(users.getUsername());
    }

    private void validateUserDoesNotExist(UsersRequestDTO usersRequestDTO) {
        List<Users> users = this.usersRepository.findByEmailOrUsername(usersRequestDTO.email(), usersRequestDTO.username());

        boolean emailExists = users.stream().anyMatch(user -> user.getEmail().equals(usersRequestDTO.email()));
        boolean usernameExists = users.stream().anyMatch(user -> user.getUsername().equals(usersRequestDTO.username()));

        if (emailExists && usernameExists) {
            throw new UserAlreadyExistsException("Email " + usersRequestDTO.email() + " and Username " + usersRequestDTO.username() + " are already in use.");
        } else if (emailExists) {
            throw new UserAlreadyExistsException("Email " + usersRequestDTO.email() + " is already in use.");
        } else if (usernameExists) {
            throw new UserAlreadyExistsException("Username " + usersRequestDTO.username() + " is already in use.");
        }
    }


    public Users getUserById(Long userId){
        return this.usersRepository.findUsersById(userId).orElseThrow(()
                -> new UserNotFoundException("User not found with ID: " + userId));
    }

    public Users getUserByEmail(String email){
        return this.usersRepository.findByEmail(email).orElseThrow(()
                -> new UserNotFoundException("User not found with email: " + email));
    }

    public List<UsersSummaryResponseDTO> getAllUsersDTO() {
        return usersRepository.findAllProjectedBy();
    }

    /**
     * Método responsável por atualizar um usuário
     *
     * @param userId
     * @param usersUpdateDTO
     * @return
     * retorna um objeto UsersSimpleResponseDTO com o username do usuário atualizado
     */
    public UsersSimpleResponseDTO updateUser(Long userId, @RequestBody @Validated UsersUpdateDTO usersUpdateDTO) {
        Users existingUser = getUserById(userId);

        // Atualiza os campos do usuário
        existingUser.setName(usersUpdateDTO.name());
        existingUser.setUsername(usersUpdateDTO.username());
        existingUser.setEmail(usersUpdateDTO.email());

        // Salva o usuário atualizado
        Users updatedUser = this.usersRepository.save(existingUser);

        return new UsersSimpleResponseDTO(updatedUser.getUsername());
    }

    /**
     * Método responsável por excluir um usuário
     *
     * @param userId
     */
    public void deleteUser(Long userId) {
        Users existingUser = getUserById(userId);
        this.usersRepository.delete(existingUser);
    }
}
