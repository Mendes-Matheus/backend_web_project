package mendes.matheus.backend_web_project.infrastructure.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@MappedSuperclass
public class PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Version
//    private Long version;

}
