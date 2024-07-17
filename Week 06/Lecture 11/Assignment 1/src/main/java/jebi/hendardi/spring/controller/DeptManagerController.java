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

import jebi.hendardi.spring.entity.DeptManager;
import jebi.hendardi.spring.entity.DeptManagerId;
import jebi.hendardi.spring.service.DeptManagerService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/deptManagers")
@RequiredArgsConstructor
public class DeptManagerController {
    private final DeptManagerService deptManagerService;

    @GetMapping
    public List<DeptManager> getAllDeptManagers() {
        return deptManagerService.getAllDeptManagers();
    }

    @GetMapping("/{empNo}/{deptNo}/{fromDate}")
    public ResponseEntity<DeptManager> getDeptManagerById(@PathVariable Integer empNo, @PathVariable String deptNo, @PathVariable String fromDate) {
        DeptManagerId id = new DeptManagerId(empNo, deptNo, java.sql.Date.valueOf(fromDate));
        return deptManagerService.getDeptManagerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public DeptManager createDeptManager(@RequestBody DeptManager deptManager) {
        return deptManagerService.createOrUpdateDeptManager(deptManager);
    }

    @PutMapping("/{empNo}/{deptNo}/{fromDate}")
    public DeptManager updateDeptManager(@PathVariable Integer empNo, @PathVariable String deptNo, @PathVariable String fromDate, @RequestBody DeptManager deptManagerUpdate) {
        DeptManagerId id = new DeptManagerId(empNo, deptNo, java.sql.Date.valueOf(fromDate));
        deptManagerUpdate.setId(id);
        return deptManagerService.createOrUpdateDeptManager(deptManagerUpdate);
    }

    @DeleteMapping("/{empNo}/{deptNo}/{fromDate}")
    public ResponseEntity<?> deleteDeptManager(@PathVariable Integer empNo, @PathVariable String deptNo, @PathVariable String fromDate) {
        DeptManagerId id = new DeptManagerId(empNo, deptNo, java.sql.Date.valueOf(fromDate));
        deptManagerService.deleteDeptManager(id);
        return ResponseEntity.ok().build();
    }
}
