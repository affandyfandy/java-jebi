package jebi.hendardi.spring.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "dept_manager")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeptManager {
    @EmbeddedId
    private DeptManagerId id;

    @MapsId("empNo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_no", nullable = false)
    private Employee employee;

    @MapsId("deptNo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_no", nullable = false)
    private Department department;

    @Column(nullable = false)
    private Date toDate;
}
