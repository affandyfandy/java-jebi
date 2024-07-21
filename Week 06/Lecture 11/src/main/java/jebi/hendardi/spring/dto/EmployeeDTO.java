package jebi.hendardi.spring.dto;

import java.util.Date;

import lombok.Data;

@Data
public class EmployeeDTO {
    private Date birthDate;
    private String firstName;
    private String lastName;
    private String gender;
    private Date hireDate;
    private Integer lastSalary;
    private String lastTitle;
}
