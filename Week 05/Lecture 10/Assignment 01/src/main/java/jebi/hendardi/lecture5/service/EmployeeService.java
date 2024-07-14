package jebi.hendardi.lecture5.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jebi.hendardi.lecture5.dto.EmployeeDTO;
import jebi.hendardi.lecture5.mapper.EmployeeMapper;
import jebi.hendardi.lecture5.model.Employee;
import jebi.hendardi.lecture5.repository.EmployeeRepository;

@Service
@Validated
@Transactional
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper = EmployeeMapper.INSTANCE;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeeDTO> findEmployeesByDepartment(String department) {
        List<Employee> employees = employeeRepository.findEmployeesByDepartmentIgnoreCase(department);
        return employeeMapper.toDTOs(employees);
    }

    public EmployeeDTO addEmployee(@Valid EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.toEntity(employeeDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDTO(savedEmployee);
    }

    public List<EmployeeDTO> findAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employeeMapper.toDTOs(employees);
    }

    public EmployeeDTO updateEmployee(@Valid EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.toEntity(employeeDTO);
        Employee updatedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDTO(updatedEmployee);
    }

    public EmployeeDTO findEmployeeById(UUID id) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        return employeeMapper.toDTO(employee);
    }

    public void deleteEmployee(UUID id) {
        employeeRepository.deleteEmployeeById(id);
    }
}
