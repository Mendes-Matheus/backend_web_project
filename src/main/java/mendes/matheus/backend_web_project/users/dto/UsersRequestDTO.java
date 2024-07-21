package mendes.matheus.backend_web_project.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsersRequestDTO(

        @JsonProperty(value = "name", required = true)
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
        @NotBlank(message = "Name is required")
        String name,

        @JsonProperty(value = "email")
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,

        @JsonProperty(value = "username")
        @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
        @NotBlank(message = "Username is required")
        String username,

        @JsonProperty(value = "password")
        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
        String password
) {}
