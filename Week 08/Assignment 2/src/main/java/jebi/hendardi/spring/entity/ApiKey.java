package jebi.hendardi.spring.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ApiKey {
    @Id
    private String id;
    private String key;
}
