package jebi.hendardi.assignment.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PrototypeService prototypeService1;

    @Autowired
    private PrototypeService prototypeService2;

    @Test
    public void testNotifyEmployeeByConstructor() {
        employeeService.notifyEmployeeByConstructor("jebi@example.com", "Work Update : testNotifyEmployeeByConstructor", "Here are your work details > ByConstructor...");
    }

    @Test
    public void testNotifyEmployeeByField() {
        employeeService.notifyEmployeeByField("nite@example.com", "Work Update : testNotifyEmployeeByField", "Here are your work details > ByField...");
    }

    @Test
    public void testNotifyEmployeeBySetter() {
        employeeService.notifyEmployeeBySetter("light@example.com", "Work Update : testNotifyEmployeeBySetter", "Here are your work details > BySetter...");
    }

    @Test
    public void testPrototypeScope() {
        System.out.println("Prototype Service 1:");
        prototypeService1.printPrototypeScope();

        System.out.println("Prototype Service 2:");
        prototypeService2.printPrototypeScope();
    }
}
