package jebi.hendardi.spring.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;
import java.util.Set;

@Entity
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int empNo;

    @Column(nullable = false)
    private Date birthDate;

    @Column(nullable = false, length = 14)
    private String firstName;

    @Column(nullable = false, length = 16)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private Date hireDate;

    @OneToMany(mappedBy = "employee")
    private Set<DeptEmp> deptEmps;

    @OneToMany(mappedBy = "employee")
    private Set<DeptManager> deptManagers;

    @OneToMany(mappedBy = "employee")
    private Set<Salary> salaries;

    @OneToMany(mappedBy = "employee")
    private Set<Title> titles;
}
