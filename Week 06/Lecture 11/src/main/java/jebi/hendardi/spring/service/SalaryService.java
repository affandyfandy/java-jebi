package jebi.hendardi.spring.service;

import jebi.hendardi.spring.entity.Salary;
import jebi.hendardi.spring.entity.SalaryId;
import jebi.hendardi.spring.repository.SalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalaryService {

    @Autowired
    private SalaryRepository salaryRepository;

    public Salary addSalary(Salary salary) {
        return salaryRepository.save(salary);
    }

    public Salary updateSalary(int empNo, Salary salary) {
        SalaryId salaryId = new SalaryId();
        salaryId.setEmployee(empNo);
        salaryId.setFromDate(salary.getFromDate());

        if (salaryRepository.existsById(salaryId)) {
            return salaryRepository.save(salary);
        }
        return null;
    }
}
