package jebi.hendardi.spring.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "salaries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Salary {
    @EmbeddedId
    private SalaryId id;

    @Column(nullable = false)
    private Integer salary;

    @MapsId("empNo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_no", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private Date toDate;
}
