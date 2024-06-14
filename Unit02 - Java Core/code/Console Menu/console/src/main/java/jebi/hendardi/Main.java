package jebi.hendardi;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String FILE_NAME = "employees.csv";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AppManager appManager = AppManager.getInstance();

        int option = -1;
        while (option != 0) {
            printMenu();
            try {
                option = scanner.nextInt();
                scanner.nextLine(); // Consume newline character after nextInt()

                appManager.handleMenu(option);
            } catch (Exception e) {
                System.out.println("Error: Invalid input. Please enter a valid option.");
                scanner.nextLine(); // Consume invalid input to avoid infinite loop
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\nMenu:");
        System.out.println("0 - break");
        System.out.println("1 - Select file, import data");
        System.out.println("2 - Add new Employee");
        System.out.println("3 - Filter by name (like), id (like), dobirth - year (equal), department (equal)");
        System.out.println("4 - Filter and export to csv file, order by Dobirth");
        System.out.print("Choose your option : ");
    }
}
