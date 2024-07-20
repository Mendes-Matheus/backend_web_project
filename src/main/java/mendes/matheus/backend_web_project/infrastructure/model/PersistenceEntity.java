package mendes.matheus.backend_web_project.infrastructure.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@MappedSuperclass
public class PersistenceEntity {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

//    @Version
//    private Long version;

}
