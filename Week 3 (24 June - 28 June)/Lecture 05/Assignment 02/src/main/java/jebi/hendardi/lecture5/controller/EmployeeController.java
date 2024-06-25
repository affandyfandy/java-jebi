package jebi.hendardi.lecture5.controller;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jebi.hendardi.lecture5.model.Employee;
import jebi.hendardi.lecture5.service.EmployeeService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final JobLauncher jobLauncher;
    private final Job job;

    public EmployeeController(EmployeeService employeeService, JobLauncher jobLauncher, Job job) {
        this.employeeService = employeeService;
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.findAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/find/{employeeID}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("employeeID") String employeeID) {  
        Employee employee = employeeService.findEmployeeByEmployeeID(employeeID);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        Employee newEmployee = employeeService.addEmployee(employee);
        return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
    }

    @GetMapping("/{department}")
    public ResponseEntity<List<Employee>> getEmployeesByDepartment(@PathVariable("department") String department) {
        List<Employee> employees = employeeService.findEmployeesByDepartment(department);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @PutMapping("/update/{employeeID}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("employeeID") String employeeID, @RequestBody Employee updatedEmployee) { 
        Employee existingEmployee = employeeService.findEmployeeByEmployeeID(employeeID);
        if (existingEmployee == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        updatedEmployee.setEmployeeID(employeeID);  
        Employee updated = employeeService.updateEmployee(updatedEmployee);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{employeeID}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("employeeID") String employeeID) { 
        employeeService.deleteEmployee(employeeID);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/import")
    public ResponseEntity<String> importCsvToDBJob() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        try {
            jobLauncher.run(job, jobParameters);
            return new ResponseEntity<>("CSV Import success.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("CSV Import failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
