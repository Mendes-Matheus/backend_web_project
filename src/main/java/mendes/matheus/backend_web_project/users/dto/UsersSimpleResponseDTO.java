package mendes.matheus.backend_web_project.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UsersSimpleResponseDTO (

        @JsonProperty(value = "username")
        String username
) {}
