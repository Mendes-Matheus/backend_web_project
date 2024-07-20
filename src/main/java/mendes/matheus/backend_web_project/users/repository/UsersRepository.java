package mendes.matheus.backend_web_project.users.repository;

import mendes.matheus.backend_web_project.users.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, String> {
}
