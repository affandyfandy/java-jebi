package jebi.hendardi.spring.controller;

import jebi.hendardi.spring.entity.Salary;
import jebi.hendardi.spring.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/salaries")
public class SalaryController {

    @Autowired
    private SalaryService salaryService;

    @PostMapping
    public Salary addSalary(@RequestBody Salary salary) {
        return salaryService.addSalary(salary);
    }

    @PutMapping("/{empNo}")
    public Salary updateSalary(@PathVariable int empNo, @RequestBody Salary salary) {
        return salaryService.updateSalary(empNo, salary);
    }
}
