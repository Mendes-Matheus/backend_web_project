package mendes.matheus.backend_web_project.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UsersSummaryResponseDTO {

        @JsonProperty(value = "id")
        private Long id;

        @JsonProperty(value = "name")
        private String name;

        @JsonProperty(value = "username")
        private String username;

        @JsonProperty(value = "city")
        private String city;

        @JsonProperty(value = "state")
        private String state;

        @JsonProperty(value = "street")
        private String street;

}
