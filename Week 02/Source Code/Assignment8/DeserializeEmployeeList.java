import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public class DeserializeEmployeeList {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        List<Employee> employeeList = null;

        try (FileInputStream fileIn = new FileInputStream("employees.ser");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            employeeList = (List<Employee>) in.readObject();
            System.out.println("Deserialized employee list:");
            for (Employee employee : employeeList) {
                System.out.println(employee);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
