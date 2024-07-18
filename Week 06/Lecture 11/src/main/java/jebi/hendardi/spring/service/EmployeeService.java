package jebi.hendardi.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jebi.hendardi.spring.dto.EmployeeDTO;
import jebi.hendardi.spring.entity.Employee;
import jebi.hendardi.spring.entity.Gender;
import jebi.hendardi.spring.entity.Salary;
import jebi.hendardi.spring.entity.Title;
import jebi.hendardi.spring.repository.EmployeeRepository;
import jebi.hendardi.spring.repository.SalaryRepository;
import jebi.hendardi.spring.repository.TitleRepository;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SalaryRepository salaryRepository;

    @Autowired
    private TitleRepository titleRepository;

    public Page<EmployeeDTO> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable).map(employee -> {
            EmployeeDTO employeeDTO = mapToDTO(employee);
            Salary lastSalary = salaryRepository.findTopByEmployeeOrderByToDateDesc(employee.getEmpNo());
            Title lastTitle = titleRepository.findTopByEmployeeOrderByToDateDesc(employee.getEmpNo());
            if (lastSalary != null) {
                employeeDTO.setLastSalary(lastSalary.getSalary());
            }
            if (lastTitle != null) {
                employeeDTO.setLastTitle(lastTitle.getTitle());
            }
            return employeeDTO;
        });
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

    private EmployeeDTO mapToDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setBirthDate(employee.getBirthDate());
        employeeDTO.setFirstName(employee.getFirstName());
        employeeDTO.setLastName(employee.getLastName());
        employeeDTO.setGender(employee.getGender().name());
        employeeDTO.setHireDate(employee.getHireDate());
        return employeeDTO;
    }
}
