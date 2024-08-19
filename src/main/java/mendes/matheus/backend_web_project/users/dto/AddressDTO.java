package mendes.matheus.backend_web_project.users.dto;

import lombok.Data;

public record AddressDTO (
        String logradouro,
        String bairro,
        String localidade,
        String uf
) {}
