package com.lecture8.assignment3.service;

import java.util.List;
import com.lecture8.assignment3.entity.Employee;

public interface EmployeeService {

    Employee save(String db, Employee employee);
    List<Employee> findAll(String db);
    Employee findEmployeeById(String db, String id);
    Employee update(String db, Employee employee);
    String delete(String db, String id);
}
