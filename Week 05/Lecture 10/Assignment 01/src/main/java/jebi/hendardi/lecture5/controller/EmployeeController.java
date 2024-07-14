package jebi.hendardi.lecture5.controller;

import jakarta.validation.Valid;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jebi.hendardi.lecture5.dto.EmployeeDTO;
import jebi.hendardi.lecture5.service.EmployeeService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> employees = employeeService.findAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable("id") UUID id) {
        EmployeeDTO employee = employeeService.findEmployeeById(id);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @PostMapping("/add")
public ResponseEntity<EmployeeDTO> addEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
    EmployeeDTO newEmployee = employeeService.addEmployee(employeeDTO);
    return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
}

    @GetMapping("/{department}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByDepartment(@PathVariable("department") String department) {
        List<EmployeeDTO> employees = employeeService.findEmployeesByDepartment(department);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable("id") UUID id, @Valid @RequestBody EmployeeDTO updatedEmployeeDTO) {
    EmployeeDTO existingEmployeeDTO = employeeService.findEmployeeById(id);
    if (existingEmployeeDTO == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    updatedEmployeeDTO.setId(id);
    EmployeeDTO updated = employeeService.updateEmployee(updatedEmployeeDTO);
    return new ResponseEntity<>(updated, HttpStatus.OK);
}

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") UUID id) {
        employeeService.deleteEmployee(id);
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

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCsvFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("File is empty", HttpStatus.BAD_REQUEST);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            List<EmployeeDTO> employeeDTOs = reader.lines()
                    .skip(1)
                    .map(this::mapToEmployeeDTO)
                    .collect(Collectors.toList());
            employeeDTOs.forEach(employeeService::addEmployee);
            return new ResponseEntity<>("File uploaded and data saved successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to process file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private EmployeeDTO mapToEmployeeDTO(String line) {
        String[] fields = line.split(",");
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName(fields[0]);
        employeeDTO.setDob(fields[1]);
        employeeDTO.setPhone(fields[2]);
        employeeDTO.setEmail(fields[3]);
        employeeDTO.setAddress(fields[4]);
        employeeDTO.setDepartment(fields[5]);
        return employeeDTO;
    }
}
