package jebi.hendardi.spring.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jebi.hendardi.spring.model.Employee;

public class FileUtils {
    public static List<Employee> readEmployeesFromCSV(MultipartFile file) throws IOException {
        List<Employee> employees = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] attributes = line.split(",");
                employees.add(fromCSV(attributes));
            }
        } catch (IOException e) {
            throw new IOException("Error reading employee (Manual) " + e);
        } 
        return employees;
    }

    public static Employee fromCSV(String[] attributes) {
        return new Employee(attributes[0], attributes[1], DateUtils.parseDate(attributes[2]),
                attributes[3], attributes[4]);
    }
}
