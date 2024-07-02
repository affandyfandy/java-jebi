package jebi.hendardi.assignment.controller;

import jebi.hendardi.assignment.service.EmployeeService;
import jebi.hendardi.assignment.service.PrototypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    private PrototypeService prototypeService1;

    @Autowired
    private PrototypeService prototypeService2;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/notify-constructor")
    public String notifyEmployeeByConstructor() {
        employeeService.notifyEmployeeByConstructor("employee@example.com", "Work Update", "Here are your work details...");
        return "Notification sent via constructor injection!";
    }

    @GetMapping("/notify-field")
    public String notifyEmployeeByField() {
        employeeService.notifyEmployeeByField("employee@example.com", "Work Update", "Here are your work details...");
        return "Notification sent via field injection!";
    }

    @GetMapping("/notify-setter")
    public String notifyEmployeeBySetter() {
        employeeService.notifyEmployeeBySetter("employee@example.com", "Work Update", "Here are your work details...");
        return "Notification sent via setter injection!";
    }

    @GetMapping("/prototype-scope")
    public String testPrototypeScope() {
        String prototype1Details = "Prototype Service1: " + prototypeService1;
        String prototype2Details = "Prototype Service2: " + prototypeService2;

        System.out.println(prototype1Details);
        System.out.println(prototype2Details);

        return prototype1Details + "\n" + prototype2Details;
    }
}
