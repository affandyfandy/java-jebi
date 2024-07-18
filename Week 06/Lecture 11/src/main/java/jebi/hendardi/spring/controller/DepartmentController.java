package jebi.hendardi.spring.controller;

import jebi.hendardi.spring.entity.Department;
import jebi.hendardi.spring.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @PostMapping
    public Department createDepartment(@RequestBody Department department) {
        return departmentService.createDepartment(department);
    }

    @PutMapping("/{deptNo}")
    public Department updateDepartment(@PathVariable String deptNo, @RequestBody Department department) {
        return departmentService.updateDepartment(deptNo, department);
    }

    @DeleteMapping("/{deptNo}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable String deptNo) {
        departmentService.deleteDepartment(deptNo);
        return ResponseEntity.noContent().build();
    }
}
