package com.lecture8.assignment3.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lecture8.assignment3.entity.Employee;
import com.lecture8.assignment3.repository.EmployeeRepository;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee save(String db, Employee employee) {
        int res = employeeRepository.save(db, employee);
        if (res > 0){
            return employee;
        }
        return null;
    }

    @Override
    public List<Employee> findAll(String db) {
        var listEmployee = employeeRepository.findAll(db);
        return listEmployee;
    }

    @Override
    public Employee findEmployeeById(String db, String id) {
        var employee = employeeRepository.findById(db, id);
        if (employee != null){
            return employee;
        }
        return null;
    }

    @Override
    public Employee update(String db, Employee employee) {
        int res = employeeRepository.update(db, employee);
        if (res > 0){
            return employee;
        }
        return null;
    }

    @Override
    public String delete(String db, String id) {
        int res = employeeRepository.deleteById(db, id);
        if (res > 0){
            return "success";
        }
        return "failed";
    }
}
