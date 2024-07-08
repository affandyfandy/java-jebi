package jebi.hendardi.assignment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class EmployeeService {

    private final EmailService emailService;

    // Constructor-based injection
    public EmployeeService(EmailService emailService) {
        this.emailService = emailService;
    }

    // Field-based injection
    @Autowired
    private EmailService fieldEmailService;

    // Setter-based injection
    private EmailService setterEmailService;

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.setterEmailService = emailService;
    }

    public void notifyEmployeeByConstructor(String to, String subject, String body) {
        emailService.sendEmail(to, subject, body);
        System.out.println("Notify by constructor: " + this);
    }

    public void notifyEmployeeByField(String to, String subject, String body) {
        fieldEmailService.sendEmail(to, subject, body);
        System.out.println("Notify by field: " + this);
    }

    public void notifyEmployeeBySetter(String to, String subject, String body) {
        setterEmailService.sendEmail(to, subject, body);
        System.out.println("Notify by setter: " + this);
    }
}
