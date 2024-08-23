package mendes.matheus.backend_web_project.users.service;

import lombok.RequiredArgsConstructor;
import mendes.matheus.backend_web_project.infrastructure.mapper.ObjectMapperUtil;
import mendes.matheus.backend_web_project.users.dto.*;
import mendes.matheus.backend_web_project.users.exceptions.UserAlreadyExistsException;
import mendes.matheus.backend_web_project.users.exceptions.UserNotFoundException;
import mendes.matheus.backend_web_project.users.model.Users;
import mendes.matheus.backend_web_project.users.repository.UsersRepository;
import org.springframework.data.domain.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
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
    private final ViaCepService viaCepService;
    private static final Logger log = LoggerFactory.getLogger(UsersService.class);


    /**
     * Método responsável por criar um usuário
     *
     * @param usersRequestDTO
     * @return
     * retorna um objeto UsersIdDTO com o ID do usuário criado
     */
    @Transactional
    public Mono<UsersSimpleResponseDTO> createUser(UsersRequestDTO usersRequestDTO) {
        return Mono.fromCallable(() -> {
                    // Verifica se já existe um usuário com o email ou username fornecido
                    validateUserDoesNotExist(usersRequestDTO);
                    return usersRequestDTO;
                })
                .flatMap(reqDTO ->
                        viaCepService.getAddressByCep(reqDTO.cep())  // Obtém um Mono<AddressDTO>
                                .retry(5)  // Tenta novamente até 3 vezes em caso de falha
                                .timeout(Duration.ofSeconds(10))  // Define timeout de 5 segundos
                                .onErrorResume(e -> {
                                    log.error("Failed to fetch address from ViaCep", e);
                                    // Fornece um AddressDTO padrão aqui
                                    return Mono.just(new AddressDTO("N/A", "N/A", "N/A", "N/A"));
                                })
                )
                .map(address -> {
                    Users users = objectMapperUtil.map(usersRequestDTO, Users.class);

                    // Atualiza os dados apenas se o endereço não for padrão
                    if (!"N/A".equals(address.localidade())) {
                        users.setCity(address.localidade());
                        users.setState(address.uf());
                        users.setStreet(address.logradouro());
                    }

                    return users;
                })

                .subscribeOn(Schedulers.boundedElastic()) // Executa em thread separada
                .map(usersRepository::save) // Operação bloqueante
                .map(savedUser -> new UsersSimpleResponseDTO(savedUser.getUsername()));
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

    public Page<UsersResponseClassDTO> getAllUsersDTOPageable(Pageable pageRequest) {
        Page<Users> usersPage = usersRepository.findAllUsersPageable(pageRequest);
        return usersPage.map(user -> objectMapperUtil.map(user, UsersResponseClassDTO.class));
    }

    public List<UsersResponseClassDTO> getAllUsersDTO() {
        List<Users> usersList = usersRepository.findAllUsers();
        return usersList.stream()
                .map(user -> objectMapperUtil.map(user, UsersResponseClassDTO.class))
                .collect(Collectors.toList());
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
