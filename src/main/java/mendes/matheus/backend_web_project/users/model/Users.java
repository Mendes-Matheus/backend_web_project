package mendes.matheus.backend_web_project.users.model;


import jakarta.persistence.*;
import lombok.*;
import mendes.matheus.backend_web_project.infrastructure.model.PersistenceEntity;

import java.io.Serializable;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Users extends PersistenceEntity implements Serializable {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

}

