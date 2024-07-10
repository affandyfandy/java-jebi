package jebi.hendardi.spring.service;

import java.util.List;

import jebi.hendardi.spring.model.Employee;

public interface EmployeeService {
    List<Employee> findAll();

    Employee findById(int theId);

    void save(Employee theEmployee);

    void deleteById(int theId);
}