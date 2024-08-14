package mendes.matheus.backend_web_project.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UsersResponseDTO(

        @JsonProperty(value = "id")
        Long id,

        @JsonProperty(value = "name")
        String name,

        @JsonProperty(value = "email")
        String email,

        @JsonProperty(value = "username")
        String username
) {}
