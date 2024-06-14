package jebi.hendardi;

public class AppManager {
    private static AppManager instance = null;

    private AppManager() {
        // Constructor private untuk singleton
    }

    public static AppManager getInstance() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    // Method untuk meng-handle menu
    public void handleMenu(int option) {
        switch (option) {
            case 1:
                selectFileAndImportData();
                break;
            case 2:
                addNewEmployee();
                break;
            case 3:
                filterData();
                break;
            case 4:
                filterAndExportToCsv();
                break;
            case 0:
                System.out.println("Exiting program...");
                break;
            default:
                System.out.println("Invalid option. Please choose again.");
                break;
        }
    }

    // Method untuk memilih file dan mengimpor data
    private void selectFileAndImportData() {
        // Implementasi untuk memilih file dan mengimpor data
        System.out.println("Selected option 1: Select file, import data");
    }

    // Method untuk menambah karyawan baru
    private void addNewEmployee() {
        // Implementasi untuk menambah karyawan baru
        System.out.println("Selected option 2: Add new Employee");
    }

    // Method untuk melakukan filter data
    private void filterData() {
        // Implementasi untuk melakukan filter data
        System.out.println("Selected option 3: Filter by name, id, dobirth, department");
    }

    // Method untuk melakukan filter dan ekspor ke file CSV
    private void filterAndExportToCsv() {
        // Implementasi untuk filter dan ekspor ke file CSV
        System.out.println("Selected option 4: Filter and export to csv file, order by Dobirth");
    }
}
