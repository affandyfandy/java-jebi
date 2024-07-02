package jebi.hendardi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
public class Employee {
    private String id;
    private String name;
    private LocalDate dateOfBirth;
    private String address;
    private String department;

    public String toCSV() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        return id + "," + name + "," + dateOfBirth.format(formatter) + "," + address + "," + department;
    }
}
