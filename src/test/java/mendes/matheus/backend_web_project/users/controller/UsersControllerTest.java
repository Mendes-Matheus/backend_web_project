package mendes.matheus.backend_web_project.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mendes.matheus.backend_web_project.users.dto.UsersRequestDTO;
import mendes.matheus.backend_web_project.users.dto.UsersSimpleResponseDTO;
import mendes.matheus.backend_web_project.users.service.UsersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@WebMvcTest(UsersController.class)
public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsersService usersService;

    @Test
    public void createUser_ShouldReturnCreatedStatus_WhenUserIsCreated() throws Exception {
        UsersRequestDTO requestDTO = new UsersRequestDTO("testuser", "testuser@gmail.com", "testuser", "password123");
        UsersSimpleResponseDTO responseDTO = new UsersSimpleResponseDTO("testuser");

        when(usersService.createUser(any(UsersRequestDTO.class))).thenReturn(responseDTO);

        String requestJson = objectMapper.writeValueAsString(requestDTO); // Converter objeto para JSON

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)) // Usar JSON gerado a partir do objeto
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    public void getUserById_ShouldReturnUser_WhenUserExists() throws Exception {
        UsersSimpleResponseDTO responseDTO = new UsersSimpleResponseDTO("testuser");

        when(usersService.getUserById(anyLong())).thenReturn(responseDTO);

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    public void deleteUser_ShouldReturnNoContentStatus_WhenUserIsDeleted() throws Exception {
        doNothing().when(usersService).deleteUser(anyLong());

        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isNoContent());
    }

    // Testes semelhantes podem ser escritos para os métodos restantes
}
