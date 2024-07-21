package jebi.hendardi.spring.service;

import jebi.hendardi.spring.entity.Department;
import jebi.hendardi.spring.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public Department updateDepartment(String deptNo, Department department) {
        if (departmentRepository.existsById(deptNo)) {
            return departmentRepository.save(department);
        }
        return null;
    }

    public void deleteDepartment(String deptNo) {
        departmentRepository.deleteById(deptNo);
    }
}
