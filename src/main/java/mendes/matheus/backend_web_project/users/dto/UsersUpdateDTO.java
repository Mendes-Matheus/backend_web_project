package mendes.matheus.backend_web_project.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsersUpdateDTO(
        @NotBlank(message = "Name is mandatory")
        String name,

        @NotBlank(message = "Username is mandatory")
        String username,

        @Email(message = "Email should be valid")
        @NotBlank(message = "Email is mandatory")
        String email
) {}
