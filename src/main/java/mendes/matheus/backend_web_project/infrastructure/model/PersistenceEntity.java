package mendes.matheus.backend_web_project.infrastructure.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@MappedSuperclass
public class PersistenceEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Version
//    private Long version;

}
