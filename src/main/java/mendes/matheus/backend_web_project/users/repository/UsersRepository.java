package mendes.matheus.backend_web_project.users.repository;

import mendes.matheus.backend_web_project.users.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);

    Optional<Users> findByUsername(String username);

    @Query("SELECT u FROM Users u")
    Page<Users> findAllUsersPageable(Pageable pageable);

    @Query("SELECT u FROM Users u")
    List<Users> findAllUsers();

    @Query("SELECT u FROM Users u WHERE u.email = :email OR u.username = :username")
    List<Users> findByEmailOrUsername(@Param("email") String email, @Param("username") String username);
}
