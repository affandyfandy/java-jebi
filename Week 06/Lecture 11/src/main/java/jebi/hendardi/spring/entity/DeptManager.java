package jebi.hendardi.spring.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Entity
@IdClass(DeptManagerId.class)
@Data
public class DeptManager implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "dept_no")
    private Department department;

    @Id
    @ManyToOne
    @JoinColumn(name = "emp_no")
    private Employee employee;

    @Column(nullable = false)
    private Date fromDate;

    @Column(nullable = false)
    private Date toDate;
}
