package jebi.hendardi.spring.service;

import jebi.hendardi.spring.entity.Salary;
import jebi.hendardi.spring.entity.SalaryId;
import jebi.hendardi.spring.repository.SalaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalaryService {
    private final SalaryRepository salaryRepository;

    public List<Salary> getAllSalaries() {
        return salaryRepository.findAll();
    }

    public Optional<Salary> getSalaryById(SalaryId id) {
        return salaryRepository.findById(id);
    }

    public Salary createOrUpdateSalary(Salary salary) {
        return salaryRepository.save(salary);
    }

    public void deleteSalary(SalaryId id) {
        salaryRepository.deleteById(id);
    }
}
