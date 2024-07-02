package jebi.hendardi.lecture5.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ContactDTO {
    private String id;
    private String name;
    private String dob;  
    private Integer age;
    private String email;
}
