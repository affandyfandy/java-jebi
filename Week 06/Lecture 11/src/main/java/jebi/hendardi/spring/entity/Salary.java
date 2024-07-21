package jebi.hendardi.spring.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Entity
@IdClass(SalaryId.class)
@Data
public class Salary implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "emp_no")
    private Employee employee;

    @Id
    @Column(nullable = false)
    private Date fromDate;

    @Column(nullable = false)
    private int salary;

    @Column(nullable = false)
    private Date toDate;
}
