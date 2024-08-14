package mendes.matheus.backend_web_project.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UsersSummaryResponseDTO(

        @JsonProperty(value = "id")
        Long id,

        @JsonProperty(value = "name")
        String name,

        @JsonProperty(value = "username")
        String username
) { }
