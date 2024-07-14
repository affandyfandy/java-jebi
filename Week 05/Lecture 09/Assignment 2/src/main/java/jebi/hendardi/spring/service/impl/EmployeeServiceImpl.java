package jebi.hendardi.spring.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jebi.hendardi.spring.model.Employee;
import jebi.hendardi.spring.repository.EmployeeRepository;
import jebi.hendardi.spring.service.EmployeeService;
import jebi.hendardi.spring.utils.FileUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAllByOrderByNameAsc();
    }

    @Override
    public Page<Employee> findAll(Pageable pageable) {
        if (pageable == null) {
            throw new IllegalArgumentException("Invalid pagination and sorting parameters: null object");
        }
        return employeeRepository.findAllByOrderByNameAsc(pageable);
    }

    @Override
    public Employee findById(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Invalid employee identifier: null or empty string");
        }
        return employeeRepository.findById(id).orElseThrow();
    }

    @Override
    public void save(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Invalid employee: null object");
        }
        employeeRepository.save(employee);
    }

    @Override
    public void deleteById(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Invalid employee identifier: null or empty string");
        }
        employeeRepository.deleteById(id);
    }

    @Override
    public void uploadCsv(MultipartFile file) {
        try {
            List<Employee> employees = FileUtils.readEmployeesFromCSV(file);
            employeeRepository.saveAll(employees);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload CSV file: " + e.getMessage());
        }
    }
}
