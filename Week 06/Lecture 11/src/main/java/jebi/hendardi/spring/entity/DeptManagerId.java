package jebi.hendardi.spring.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeptManagerId implements Serializable {
    private String department;
    private int employee;
}
