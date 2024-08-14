package mendes.matheus.backend_web_project.users.service;

import mendes.matheus.backend_web_project.infrastructure.mapper.ObjectMapperUtil;
import mendes.matheus.backend_web_project.users.dto.UsersRequestDTO;
import mendes.matheus.backend_web_project.users.dto.UsersSimpleResponseDTO;
import mendes.matheus.backend_web_project.users.exceptions.UserAlreadyExistsException;
import mendes.matheus.backend_web_project.users.exceptions.UserNotFoundException;
import mendes.matheus.backend_web_project.users.model.Users;
import mendes.matheus.backend_web_project.users.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UsersServiceTest {

    @MockBean
    private UsersRepository usersRepository;

    @MockBean
    private ObjectMapperUtil objectMapperUtil;

    @Autowired
    private UsersService usersService;

    @Test
    public void createUser_ShouldCreateUser_WhenUserDoesNotExist() {
        UsersRequestDTO requestDTO = new UsersRequestDTO("test@example.com", "testuser", "Test User", "password123");
        Users user = new Users();
        user.setUsername("testuser");

        when(usersRepository.findByEmailOrUsername(anyString(), anyString())).thenReturn(Collections.emptyList());
        when(objectMapperUtil.map(requestDTO, Users.class)).thenReturn(user);
        when(usersRepository.save(user)).thenReturn(user);

        UsersSimpleResponseDTO responseDTO = usersService.createUser(requestDTO);

        assertEquals("testuser", responseDTO.username());
        verify(usersRepository, times(1)).save(user);
    }

    @Test
    public void createUser_ShouldThrowException_WhenUserExists() {
        UsersRequestDTO requestDTO = new UsersRequestDTO("test@example.com", "testuser", "Test User", "password123");
        Users existingUser = new Users();

        when(usersRepository.findByEmailOrUsername(anyString(), anyString())).thenReturn(Collections.singletonList(existingUser));

        assertThrows(UserAlreadyExistsException.class, () -> {
            usersService.createUser(requestDTO);
        });
    }

    @Test
    public void getUserById_ShouldReturnUser_WhenUserExists() {
        Users user = new Users();
        user.setUsername("testuser");

        when(usersRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UsersSimpleResponseDTO responseDTO = usersService.getUserById(1L);

        assertEquals("testuser", responseDTO.username());
    }

    @Test
    public void getUserById_ShouldThrowException_WhenUserNotFound() {
        when(usersRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            usersService.getUserById(1L);
        });
    }

    // Testes semelhantes podem ser escritos para os métodos restantes
}
