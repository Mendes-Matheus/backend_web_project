package mendes.matheus.backend_web_project.users.repository;

import mendes.matheus.backend_web_project.users.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);

    Optional<Users> findByUsername(String username);

    Optional<Users> findUsersById(Long userId);
}
