package jebi.hendardi.lecture5.config;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import jebi.hendardi.lecture5.model.Employee;

import java.util.UUID;

@Component
public class EmployeeProcessor implements ItemProcessor<Employee, Employee> {

    @Override
    public Employee process(Employee employee) throws Exception {
        // Generate a new UUID for the employee ID
        employee.setId(UUID.randomUUID());
        return employee;
    }
}
