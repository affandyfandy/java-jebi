package com.lecture8.assignment3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lecture8.assignment3.entity.Employee;
import com.lecture8.assignment3.service.EmployeeService;

@RestController
@RequestMapping("/api/v2/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/{db}")
    public ResponseEntity<List<Employee>> getAllEmployees(@PathVariable String db) {
        var listEmployee = employeeService.findAll(db);
        if (listEmployee.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listEmployee);
    }
    
    @PostMapping("/{db}/add")
    public ResponseEntity<Employee> addEmployee(@PathVariable String db, @RequestBody Employee employee) {
        var findEmployee = employeeService.findEmployeeById(db, employee.getId());
        if (findEmployee != null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(employeeService.save(db, employee));
    }

    @GetMapping("/{db}/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String db, @PathVariable String id) {
        var employee = employeeService.findEmployeeById(db, id);
        if (employee != null){
            return ResponseEntity.ok(employee);
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{db}/{id}/delete")
    public ResponseEntity<String> deleteEmployee(@PathVariable String db, @PathVariable String id){
        var employee = employeeService.findEmployeeById(db, id);
        if (employee != null){
            return ResponseEntity.ok(employeeService.delete(db, id));
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{db}/{id}/update")
    public ResponseEntity<Employee> updateEmployee(@PathVariable String db, @PathVariable String id, @RequestBody Employee employee) {
        var findEmployee = employeeService.findEmployeeById(db, id);
        if (findEmployee != null){
            employee.setId(findEmployee.getId());
            return ResponseEntity.ok(employeeService.update(db, employee));
        }
        return ResponseEntity.badRequest().build();
    }
}
