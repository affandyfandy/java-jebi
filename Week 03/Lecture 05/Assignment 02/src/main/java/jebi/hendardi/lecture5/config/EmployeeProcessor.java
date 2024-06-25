package jebi.hendardi.lecture5.config;

import jebi.hendardi.lecture5.model.Employee;
import org.springframework.batch.item.ItemProcessor;

public class EmployeeProcessor implements ItemProcessor<Employee, Employee> {

    @Override
    public Employee process(Employee employee) {
        return employee;
    }
}
