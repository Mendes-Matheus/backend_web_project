package mendes.matheus.backend_web_project.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UsersResponseDTO(

        @JsonProperty(value = "id")
        Long id,

        @JsonProperty(value = "name")
        String name,

        @JsonProperty(value = "username")
        String username,

        @JsonProperty(value = "cep")
        String cep,

        @JsonProperty(value = "city")
        String city,

        @JsonProperty(value = "state")
        String state,

        @JsonProperty(value = "street")
        String street
) {}
