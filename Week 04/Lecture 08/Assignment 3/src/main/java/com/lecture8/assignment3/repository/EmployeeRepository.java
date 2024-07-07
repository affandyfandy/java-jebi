package com.lecture8.assignment3.repository;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.lecture8.assignment3.entity.Employee;

@Repository
public class EmployeeRepository {

    private final JdbcTemplate jdbc1;
    private final JdbcTemplate jdbc2;

    @Autowired
    public EmployeeRepository(@Qualifier("dataSource1") DataSource dataSource1,
                        @Qualifier("dataSource2") DataSource dataSource2) {
        this.jdbc1 = new JdbcTemplate(dataSource1);
        this.jdbc2 = new JdbcTemplate(dataSource2);
    }

    private JdbcTemplate getJdbcTemplate(String db) {
        return "db1".equals(db) ? jdbc1 : jdbc2;
    }

    public int save(String db, Employee employee){
        return getJdbcTemplate(db).update("INSERT INTO employee (id, name, dob, address, department) VALUES (?,?,?,?,?)",
            new Object[] { employee.getId(), employee.getName(), employee.getDob(), employee.getAddress(), employee.getDepartment() });
    }

    public int update(String db, Employee employee){
        return getJdbcTemplate(db).update("UPDATE employee SET name=?, dob=?, address=?, department=? WHERE id=?",
            new Object[] { employee.getName(), employee.getDob(), employee.getAddress(), employee.getDepartment(), employee.getId()});
    }

    public Employee findById(String db, String id){
        try {
            Employee employee = getJdbcTemplate(db).queryForObject("SELECT * FROM employee WHERE id=? LIMIT 1",
                    BeanPropertyRowMapper.newInstance(Employee.class), id);
            return employee;
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    public int deleteById(String db, String id) {
        return getJdbcTemplate(db).update("DELETE FROM employee WHERE id=?", id);
    }

    public List<Employee> findAll(String db){
        return getJdbcTemplate(db).query("SELECT * from employee", BeanPropertyRowMapper.newInstance(Employee.class));
    }

}
