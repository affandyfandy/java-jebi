package jebi.hendardi.spring.service;

import jebi.hendardi.spring.dto.EmployeeDTO;
import jebi.hendardi.spring.entity.Employee;
import jebi.hendardi.spring.entity.Gender;
import jebi.hendardi.spring.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Page<Employee> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    public Employee createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setBirthDate(employeeDTO.getBirthDate());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setGender(Gender.valueOf(employeeDTO.getGender()));
        employee.setHireDate(employeeDTO.getHireDate());
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(int empNo, EmployeeDTO employeeDTO) {
        if (employeeRepository.existsById(empNo)) {
            Employee employee = employeeRepository.findById(empNo).orElse(null);
            if (employee != null) {
                employee.setBirthDate(employeeDTO.getBirthDate());
                employee.setFirstName(employeeDTO.getFirstName());
                employee.setLastName(employeeDTO.getLastName());
                employee.setGender(Gender.valueOf(employeeDTO.getGender()));
                employee.setHireDate(employeeDTO.getHireDate());
                return employeeRepository.save(employee);
            }
        }
        return null;
    }

    public void deleteEmployee(int empNo) {
        employeeRepository.deleteById(empNo);
    }
}
