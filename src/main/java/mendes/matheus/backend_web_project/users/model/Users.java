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

    private String name;
    private String email;
    private String password;

}

