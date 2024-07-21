package jebi.hendardi.spring.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SalaryId implements Serializable {
    private int employee;
    private Date fromDate;
}
