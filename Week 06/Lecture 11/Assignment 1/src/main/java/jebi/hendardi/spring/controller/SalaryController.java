package jebi.hendardi.spring.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jebi.hendardi.spring.entity.Salary;
import jebi.hendardi.spring.entity.SalaryId;
import jebi.hendardi.spring.service.SalaryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/salaries")
@RequiredArgsConstructor
public class SalaryController {
    private final SalaryService salaryService;

    @GetMapping
    public List<Salary> getAllSalaries() {
        return salaryService.getAllSalaries();
    }

    @GetMapping("/{empNo}/{fromDate}")
    public ResponseEntity<Salary> getSalaryById(@PathVariable Integer empNo, @PathVariable String fromDate) {
        SalaryId id = new SalaryId(empNo, java.sql.Date.valueOf(fromDate));
        return salaryService.getSalaryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Salary createSalary(@RequestBody Salary salary) {
        return salaryService.createOrUpdateSalary(salary);
    }

    @PutMapping("/{empNo}/{fromDate}")
    public Salary updateSalary(@PathVariable Integer empNo, @PathVariable String fromDate, @RequestBody Salary salary) {
        SalaryId id = new SalaryId(empNo, java.sql.Date.valueOf(fromDate));
        salary.setId(id);
        return salaryService.createOrUpdateSalary(salary);
    }

    @DeleteMapping("/{empNo}/{fromDate}")
    public ResponseEntity<?> deleteSalary(@PathVariable Integer empNo, @PathVariable String fromDate) {
        SalaryId id = new SalaryId(empNo, java.sql.Date.valueOf(fromDate));
        salaryService.deleteSalary(id);
        return ResponseEntity.ok().build();
    }
}
