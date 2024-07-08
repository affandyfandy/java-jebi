package com.lecture8.assignment3.repository;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.lecture8.assignment3.entity.Employee;
import com.lecture8.assignment3.mapper.EmployeeRowMapper;

@Repository
public class EmployeeRepository {

    private final JdbcTemplate jdbc1;
    private final JdbcTemplate jdbc2;

    public EmployeeRepository(@Qualifier("dataSource1") DataSource dataSource1,
                              @Qualifier("dataSource2") DataSource dataSource2) {
        this.jdbc1 = new JdbcTemplate(dataSource1);
        this.jdbc2 = new JdbcTemplate(dataSource2);
    }

    private JdbcTemplate getJdbcTemplate(String db) {
        return "db1".equals(db) ? jdbc1 : jdbc2;
    }

    public int save(String db, Employee employee) {
        try {
            return getJdbcTemplate(db).update("INSERT INTO employee (id, name, dob, address, department) VALUES (?,?,?,?,?)",
                employee.getId(), employee.getName(), employee.getDob(), employee.getAddress(), employee.getDepartment());
        } catch (DataAccessException e) {
            return 0;
        }
    }

    public int update(String db, Employee employee) {
        try {
            return getJdbcTemplate(db).update("UPDATE employee SET name=?, dob=?, address=?, department=? WHERE id=?",
                employee.getName(), employee.getDob(), employee.getAddress(), employee.getDepartment(), employee.getId());
        } catch (DataAccessException e) {
            return 0;
        }
    }

    public Employee findById(String db, String id) {
        try {
            return getJdbcTemplate(db).queryForObject("SELECT * FROM employee WHERE id=? LIMIT 1", new EmployeeRowMapper(), id);
        } catch (DataAccessException e) {
            return null;
        }
    }

    public int deleteById(String db, String id) {
        try {
            return getJdbcTemplate(db).update("DELETE FROM employee WHERE id=?", id);
        } catch (DataAccessException e) {
            return 0;
        }
    }

    public List<Employee> findAll(String db) {
        try {
            return getJdbcTemplate(db).query("SELECT * from employee", new EmployeeRowMapper());
        } catch (DataAccessException e) {
            return List.of();
        }
    }
}
