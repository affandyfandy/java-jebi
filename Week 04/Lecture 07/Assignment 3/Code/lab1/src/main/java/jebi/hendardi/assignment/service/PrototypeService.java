package jebi.hendardi.assignment.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PrototypeService {

    public void printPrototypeScope() {
        System.out.println("Prototype Service: " + this);
    }
}
