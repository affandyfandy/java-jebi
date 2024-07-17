package jebi.hendardi.spring.service;

import jebi.hendardi.spring.entity.Department;
import jebi.hendardi.spring.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Optional<Department> getDepartmentById(String id) {
        return departmentRepository.findById(id);
    }

    public Department createOrUpdateDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public void deleteDepartment(String id) {
        departmentRepository.deleteById(id);
    }
}
