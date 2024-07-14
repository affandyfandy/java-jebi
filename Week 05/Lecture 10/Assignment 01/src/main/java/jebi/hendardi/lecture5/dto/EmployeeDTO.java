package jebi.hendardi.lecture5.dto;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDTO {
    private UUID id;

    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Name must contain only letters and spaces")
    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Date of birth is mandatory")
    private String dob;

    @Pattern(regexp = "^\\+62[0-9]+$", message = "Phone must start with +62")
    @NotBlank(message = "Phone is mandatory")
    private String phone;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    private String email;

    private String address;

    private String department;
}
