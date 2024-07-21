package mendes.matheus.backend_web_project.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsersRequestDTO(

        @JsonProperty(value = "name", required = true)
        @NotBlank(message = "Name is required")
        String name,

        @JsonProperty(value = "email")
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,

        @JsonProperty(value = "username")
        @NotBlank(message = "Username is required")
        String username,

        @JsonProperty(value = "password")
        @NotBlank(message = "Password is required")
        String password
) {}
