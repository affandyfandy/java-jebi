package com.lecture8.assignment3.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lecture8.assignment3.entity.Employee;
import com.lecture8.assignment3.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional
    public Employee save(String db, Employee employee) {
        int result = employeeRepository.save(db, employee);
        return result == 1 ? employee : null; // Return null if save failed
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> findAll(String db) {
        return employeeRepository.findAll(db);
    }

    @Override
    @Transactional(readOnly = true)
    public Employee findEmployeeById(String db, String id) {
        return employeeRepository.findById(db, id);
    }

    @Override
    @Transactional
    public Employee update(String db, Employee employee) {
        int result = employeeRepository.update(db, employee);
        return result == 1 ? employee : null; // Return null if update failed
    }

    @Override
    @Transactional
    public String delete(String db, String id) {
        int result = employeeRepository.deleteById(db, id);
        return result == 1 ? "Deleted successfully" : "Deletion failed"; // Return status message
    }
}
