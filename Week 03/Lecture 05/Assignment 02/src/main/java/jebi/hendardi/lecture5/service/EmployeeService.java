package jebi.hendardi.lecture5.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jebi.hendardi.lecture5.model.Employee;
import jebi.hendardi.lecture5.repository.EmployeeRepository;

@Service
@Transactional
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findEmployeesByDepartment(String department) {
        return employeeRepository.findEmployeesByDepartmentIgnoreCase(department);
    }

    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee updateEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee findEmployeeByEmployeeID(String employeeID) {  // Mengganti Long dengan String
        return employeeRepository.findById(employeeID).orElse(null);  // Mengganti ID dengan employeeID
    }

    public void deleteEmployee(String employeeID) {  // Mengganti Long dengan String
        employeeRepository.deleteEmployeeByEmployeeID(employeeID);  // Mengganti ID dengan employeeID
    }
}
