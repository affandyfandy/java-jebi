package jebi.hendardi.spring.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Entity
@Data
public class Department {
    @Id
    @Column(length = 4)
    private String deptNo;
    
    @Column(nullable = false, length = 40)
    private String deptName;
    
    @OneToMany(mappedBy = "department")
    private Set<DeptEmp> deptEmps;
    
    @OneToMany(mappedBy = "department")
    private Set<DeptManager> deptManagers;
}
