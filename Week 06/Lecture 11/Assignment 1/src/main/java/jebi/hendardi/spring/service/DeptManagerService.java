package jebi.hendardi.spring.service;

import jebi.hendardi.spring.entity.DeptManager;
import jebi.hendardi.spring.entity.DeptManagerId;
import jebi.hendardi.spring.repository.DeptManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeptManagerService {
    private final DeptManagerRepository deptManagerRepository;

    public List<DeptManager> getAllDeptManagers() {
        return deptManagerRepository.findAll();
    }

    public Optional<DeptManager> getDeptManagerById(DeptManagerId id) {
        return deptManagerRepository.findById(id);
    }

    public DeptManager createOrUpdateDeptManager(DeptManager deptManager) {
        return deptManagerRepository.save(deptManager);
    }

    public void deleteDeptManager(DeptManagerId id) {
        deptManagerRepository.deleteById(id);
    }
}
