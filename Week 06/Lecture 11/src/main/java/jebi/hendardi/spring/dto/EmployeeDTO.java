package jebi.hendardi.spring.dto;

import lombok.Data;
import java.util.Date;

@Data
public class EmployeeDTO {
    private Date birthDate;
    private String firstName;
    private String lastName;
    private String gender;
    private Date hireDate;
}
