package jebi.hendardi.spring.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import jebi.hendardi.spring.model.Employee;

public interface EmployeeService {

    List<Employee> findAll();

    Page<Employee> findAll(Pageable pageable);

    Employee findById(String id);

    void save(Employee employee);

    void deleteById(String id);

    void uploadCsv(MultipartFile file);
}
