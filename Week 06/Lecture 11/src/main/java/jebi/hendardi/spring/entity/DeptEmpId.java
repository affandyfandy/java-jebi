package jebi.hendardi.spring.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeptEmpId implements Serializable {
    private int employee;
    private String department;
}
