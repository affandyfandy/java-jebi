package jebi.hendardi.spring.controller;

import jebi.hendardi.spring.entity.DeptEmp;
import jebi.hendardi.spring.entity.DeptEmpId;
import jebi.hendardi.spring.service.DeptEmpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deptEmps")
@RequiredArgsConstructor
public class DeptEmpController {
    private final DeptEmpService deptEmpService;

    @GetMapping
    public List<DeptEmp> getAllDeptEmps() {
        return deptEmpService.getAllDeptEmps();
    }

    @GetMapping("/{empNo}/{deptNo}/{fromDate}")
    public ResponseEntity<DeptEmp> getDeptEmpById(@PathVariable Integer empNo, @PathVariable String deptNo, @PathVariable String fromDate) {
        DeptEmpId id = new DeptEmpId(empNo, deptNo, java.sql.Date.valueOf(fromDate));
        return deptEmpService.getDeptEmpById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public DeptEmp createDeptEmp(@RequestBody DeptEmp deptEmp) {
        return deptEmpService.createOrUpdateDeptEmp(deptEmp);
    }

    @PutMapping("/{empNo}/{deptNo}/{fromDate}")
    public DeptEmp updateDeptEmp(@PathVariable Integer empNo, @PathVariable String deptNo, @PathVariable String fromDate, @RequestBody DeptEmp deptEmpUpdate) {
        DeptEmpId id = new DeptEmpId(empNo, deptNo, java.sql.Date.valueOf(fromDate));
        deptEmpUpdate.setId(id);
        return deptEmpService.createOrUpdateDeptEmp(deptEmpUpdate);
    }

    @DeleteMapping("/{empNo}/{deptNo}/{fromDate}")
    public ResponseEntity<?> deleteDeptEmp(@PathVariable Integer empNo, @PathVariable String deptNo, @PathVariable String fromDate) {
        DeptEmpId id = new DeptEmpId(empNo, deptNo, java.sql.Date.valueOf(fromDate));
        deptEmpService.deleteDeptEmp(id);
        return ResponseEntity.ok().build();
    }
}
