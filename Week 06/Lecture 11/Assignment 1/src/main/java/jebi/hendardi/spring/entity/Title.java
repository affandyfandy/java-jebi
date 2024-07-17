package jebi.hendardi.spring.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "titles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Title {
    @EmbeddedId
    private TitleId id;

    @MapsId("empNo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_no", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private Date toDate;
}
