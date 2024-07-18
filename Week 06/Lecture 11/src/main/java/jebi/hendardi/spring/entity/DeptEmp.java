package jebi.hendardi.spring.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Entity
@IdClass(DeptEmpId.class)
@Data
public class DeptEmp implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "emp_no")
    private Employee employee;

    @Id
    @ManyToOne
    @JoinColumn(name = "dept_no")
    private Department department;

    @Column(nullable = false)
    private Date fromDate;

    @Column(nullable = false)
    private Date toDate;
}
