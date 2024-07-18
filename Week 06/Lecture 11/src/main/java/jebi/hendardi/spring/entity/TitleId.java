package jebi.hendardi.spring.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TitleId implements Serializable {
    private int employee;
    private String title;
    private Date fromDate;
}
