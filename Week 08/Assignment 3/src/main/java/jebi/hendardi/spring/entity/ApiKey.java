package jebi.hendardi.spring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "apikey")
public class ApiKey {
    @Id
    private String id;

    @Column(name = "xkey")
    private String key;

    @Column(name = "username")
    private String username;

    @Column(name = "last_used")
    private LocalDateTime lastUsed;
}
