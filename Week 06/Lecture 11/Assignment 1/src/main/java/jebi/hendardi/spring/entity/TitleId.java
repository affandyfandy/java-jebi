package jebi.hendardi.spring.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TitleId implements Serializable {
    private Integer empNo;
    private String title;
    private Date fromDate;
}
