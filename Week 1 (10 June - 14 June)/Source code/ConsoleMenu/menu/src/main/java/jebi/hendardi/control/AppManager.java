package jebi.hendardi.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import jebi.hendardi.entity.Employee;
import jebi.hendardi.utils.DateUtils;
import jebi.hendardi.utils.FileUtils;

public class AppManager {
    private static AppManager instance;
    private List<Employee> employees;
    private final Scanner scanner;
    private boolean useOpenCSV;

    private AppManager() {
        employees = new ArrayList<>();
        scanner = new Scanner(System.in);
        useOpenCSV = false;
    }

    public static AppManager getInstance() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    public void start() {
        while (true) {
            showMenu();
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                System.out.println("--------------------------------------------------------------------");
                switch (choice) {
                    case 0 -> {
                        System.out.println("Exiting...");
                        System.exit(0);
                    }
                    case 1 -> importData();
                    case 2 -> addEmployee();
                    case 3 -> printFilteredEmployees();
                    case 4 -> exportFilteredEmployees();
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private void showMenu() {
        System.out.println("====================================================================");
        
        System.out.println("Menu:");
        System.out.println("0 - Exit");
        System.out.println("1 - Select File, Import data");
        System.out.println("2 - Add new Employee");
        System.out.println("3 - Filter Employees");
        System.out.println("4 - Export filtered Employees");
        System.out.print("Choose an option: ");
    }

    private void importData() {
        try {
            System.out.println("Select CSV reading method:");
            System.out.println("1 - Manual");
            System.out.println("2 - OpenCSV");
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine().trim());
            useOpenCSV = (choice == 2);

            System.out.print("Enter the file path: ");
            String filePath = scanner.nextLine().trim();

            if (filePath.isEmpty()) {
                System.out.println("File path cannot be empty. Please try again.");
                return;
            }

            List<Employee> importedEmployees;
            if (useOpenCSV) {
                importedEmployees = FileUtils.readEmployeesFromCSVOpenCSV(filePath);
            } else {
                importedEmployees = FileUtils.readEmployeesFromCSVManual(filePath);
            }

            if (importedEmployees.isEmpty()) {
                System.out.println("No employees found in the file or file could not be read.");
            } else {
                employees.addAll(importedEmployees);
                System.out.println("Imported " + importedEmployees.size() + " employees successfully.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input for reading method. Please enter a number (1 or 2).");
        } catch (Exception e) {
            System.out.println("An error occurred during import: " + e.getMessage());
        }
    }

    private void addEmployee() {
        try {
            System.out.print("Enter ID: ");
            String id = scanner.nextLine().trim();

            Employee existingEmployee = employees.stream()
                    .filter(e -> e.getId().equalsIgnoreCase(id))
                    .findFirst()
                    .orElse(null);

            if (existingEmployee != null) {
                System.out.println("Employee with ID " + id + " already exists.");
                System.out.print("Do you want to overwrite? (yes/no): ");
                String response = scanner.nextLine().trim().toLowerCase();
                if (!response.equals("yes")) {
                    System.out.println("Addition cancelled.");
                    return;
                }
                employees.remove(existingEmployee);
            }

            System.out.print("Enter Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Enter Date of Birth (d/M/yyyy): ");
            String dobString = scanner.nextLine().trim();
            System.out.print("Enter Address: ");
            String address = scanner.nextLine().trim();
            System.out.print("Enter Department: ");
            String department = scanner.nextLine().trim();

            Employee employee = new Employee(id, name, DateUtils.parseDate(dobString), address, department);
            employees.add(employee);
            System.out.println("Employee added successfully.");
        } catch (Exception e) {
            System.out.println("An error occurred while adding the employee: " + e.getMessage());
        }
    }

    private List<Employee> filterEmployees() {
        System.out.println("Filter by:");
        System.out.println("0 - All Data");
        System.out.println("1 - Name");
        System.out.println("2 - ID");
        System.out.println("3 - Date of Birth (year)");
        System.out.println("4 - Department");
        System.out.print("Choose an option: ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            List<Employee> filtered = new ArrayList<>();
            switch (choice) {
                case 0 -> {
                    // Tampilkan semua data
                    filtered = new ArrayList<>(employees);
                }
                case 1 -> {
                    System.out.print("Enter name pattern: ");
                    String namePattern = scanner.nextLine();
                    filtered = employees.stream()
                            .filter(e -> e.getName().contains(namePattern))
                            .collect(Collectors.toList());
                }
                case 2 -> {
                    System.out.print("Enter ID pattern: ");
                    String idPattern = scanner.nextLine();
                    filtered = employees.stream()
                            .filter(e -> e.getId().contains(idPattern))
                            .collect(Collectors.toList());
                }
                case 3 -> {
                    System.out.print("Enter year of birth: ");
                    int year = Integer.parseInt(scanner.nextLine().trim());
                    filtered = employees.stream()
                            .filter(e -> e.getDateOfBirth().getYear() == year)
                            .collect(Collectors.toList());
                }
                case 4 -> {
                    System.out.print("Enter Department: ");
                    String department = scanner.nextLine();
                    filtered = employees.stream()
                            .filter(e -> e.getDepartment().equalsIgnoreCase(department))
                            .collect(Collectors.toList());
                }
                default -> System.out.println("Invalid choice. Returning to menu.");
            }
            return filtered;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return new ArrayList<>();
        }
    }

    private void printFilteredEmployees() {
        List<Employee> filteredEmployees = filterEmployees();
        System.out.println("\nShowing " + filteredEmployees.size() + " employee(s) data...");
        for (Employee employee : filteredEmployees) {
            System.out.println(employee.toCSV());
        }
    }

    private void exportFilteredEmployees() {
        List<Employee> filteredEmployees = filterEmployees().stream()
                .sorted((e1, e2) -> e1.getDateOfBirth().compareTo(e2.getDateOfBirth()))
                .collect(Collectors.toList());
        System.out.print("Enter the file path to save: ");
        String filePath = scanner.nextLine();
        FileUtils.writeEmployeesToCSV(filteredEmployees, filePath);
    }
}
