package mendes.matheus.backend_web_project.users.service;

import lombok.RequiredArgsConstructor;
import mendes.matheus.backend_web_project.infrastructure.mapper.ObjectMapperUtil;
import mendes.matheus.backend_web_project.users.dto.*;
import mendes.matheus.backend_web_project.users.exceptions.UserAlreadyExistsException;
import mendes.matheus.backend_web_project.users.exceptions.UserNotFoundException;
import mendes.matheus.backend_web_project.users.model.Users;
import mendes.matheus.backend_web_project.users.repository.UsersRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Matheus Mendes
 */
@Service
@Validated
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final ObjectMapperUtil objectMapperUtil;
    private final ViaCepService viaCepService;


    /**
     * Método responsável por criar um usuário
     *
     * @param usersRequestDTO
     * @return
     * retorna um objeto UsersIdDTO com o ID do usuário criado
     */
    @Transactional
    public UsersSimpleResponseDTO createUser(@RequestBody @Validated UsersRequestDTO usersRequestDTO) {

        // Verifica se já existe um usuário com o email ou com o username fornecido
        validateUserDoesNotExist(usersRequestDTO);

        // Busca os dados do endereço pelo CEP
        AddressDTO address = viaCepService.getAddressByCep(usersRequestDTO.cep());

        // Cria o usuário com os dados do endereço
        Users users = objectMapperUtil.map(usersRequestDTO, Users.class);
        users.setCity(address.localidade());
        users.setState(address.uf());
        users.setStreet(address.logradouro());

//        Users savedUser = this.usersRepository.save(objectMapperUtil.map(usersRequestDTO, Users.class));
//        return new UsersSimpleResponseDTO(savedUser.getUsername());
        // Salva o usuário no banco de dados
        Users savedUser = usersRepository.save(users);
        return new UsersSimpleResponseDTO(savedUser.getUsername());
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

    public Users getUserEntityById(Long userId) {
        return this.usersRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User not found with ID: " + userId));
    }



    public UsersSimpleResponseDTO getUserById(Long userId){
        Users user = this.usersRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User not found with ID: " + userId));
        return new UsersSimpleResponseDTO(user.getUsername());
    }


    /**
     * Método responsável por buscar um usuário pelo username
     *
     * @param username
     * @return retorna um objeto UsersSimpleResponseDTO com o username do usuário encontrado
     */
    public UsersSimpleResponseDTO getUserByUsername(String username) {
        Users user = usersRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException("User not found with username: " + username));
        return new UsersSimpleResponseDTO(user.getUsername());
    }

    /**
     * Método responsável por buscar um usuário pelo email
     *
     * @param email
     * @return retorna um objeto UsersSimpleResponseDTO com o username do usuário encontrado
     */
    public UsersSimpleResponseDTO getUserByEmail(String email) {
        Users user = usersRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("User not found with email: " + email));
        return new UsersSimpleResponseDTO(user.getUsername());
    }

    public Page<UsersSummaryResponseDTO> getAllUsersDTO(PageRequest pageRequest) {
        Page<Users> usersPage = usersRepository.findAllUsers(pageRequest);
        return usersPage.map(user -> objectMapperUtil.map(user, UsersSummaryResponseDTO.class));
    }

    /**
     * Método responsável por atualizar um usuário
     *
     * @param userId
     * @param usersUpdateDTO
     * @return
     * retorna um objeto UsersSimpleResponseDTO com o username do usuário atualizado
     */
    @Transactional
    public UsersSimpleResponseDTO updateUser(Long userId, @RequestBody @Validated UsersUpdateDTO usersUpdateDTO) {
        Users existingUser = getUserEntityById(userId);

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
    @Transactional
    public void deleteUser(Long userId) {
        Users existingUser = getUserEntityById(userId);
        this.usersRepository.delete(existingUser);
    }

}
