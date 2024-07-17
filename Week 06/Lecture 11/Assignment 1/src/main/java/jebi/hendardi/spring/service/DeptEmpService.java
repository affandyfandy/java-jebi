package jebi.hendardi.spring.service;

import jebi.hendardi.spring.entity.DeptEmp;
import jebi.hendardi.spring.entity.DeptEmpId;
import jebi.hendardi.spring.repository.DeptEmpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeptEmpService {
    private final DeptEmpRepository deptEmpRepository;

    public List<DeptEmp> getAllDeptEmps() {
        return deptEmpRepository.findAll();
    }

    public Optional<DeptEmp> getDeptEmpById(DeptEmpId id) {
        return deptEmpRepository.findById(id);
    }

    public DeptEmp createOrUpdateDeptEmp(DeptEmp deptEmp) {
        return deptEmpRepository.save(deptEmp);
    }

    public void deleteDeptEmp(DeptEmpId id) {
        deptEmpRepository.deleteById(id);
    }
}
