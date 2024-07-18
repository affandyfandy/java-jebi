package jebi.hendardi.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jebi.hendardi.spring.dto.EmployeeDTO;
import jebi.hendardi.spring.entity.Employee;
import jebi.hendardi.spring.service.EmployeeService;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public Page<EmployeeDTO> getAllEmployees(Pageable pageable) {
        return employeeService.getAllEmployees(pageable);
    }

    @PostMapping
    public Employee createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return employeeService.createEmployee(employeeDTO);
    }

    @PutMapping("/{empNo}")
    public Employee updateEmployee(@PathVariable int empNo, @RequestBody EmployeeDTO employeeDTO) {
        return employeeService.updateEmployee(empNo, employeeDTO);
    }

    @DeleteMapping("/{empNo}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable int empNo) {
        employeeService.deleteEmployee(empNo);
        return ResponseEntity.noContent().build();
    }
}
