import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class SerializeEmployeeList {
    public static void main(String[] args) {
        List<Employee> employeeList = EmployeeListCreator.createEmployeeList();

        try (FileOutputStream fileOut = new FileOutputStream("employees.ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(employeeList);
            System.out.println("Serialized employee list is saved in employees.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}
