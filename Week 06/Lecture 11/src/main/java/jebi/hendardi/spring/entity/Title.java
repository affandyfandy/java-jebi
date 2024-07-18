package jebi.hendardi.spring.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Entity
@IdClass(TitleId.class)
@Data
public class Title implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "emp_no")
    private Employee employee;

    @Id
    @Column(nullable = false, length = 50)
    private String title;

    @Id
    @Column(nullable = false)
    private Date fromDate;

    @Column(nullable = false)
    private Date toDate;
}
