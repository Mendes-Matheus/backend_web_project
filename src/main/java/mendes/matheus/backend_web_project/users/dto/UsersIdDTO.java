package mendes.matheus.backend_web_project.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UsersIdDTO(
    @JsonProperty(value = "userId")
    Long userId
) {}
