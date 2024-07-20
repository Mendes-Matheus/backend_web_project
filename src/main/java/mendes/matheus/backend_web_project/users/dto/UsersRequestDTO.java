package mendes.matheus.backend_web_project.users.dto;

public record UsersRequestDTO(
    String name,
    String email,
    String username,
    String password
) {}
