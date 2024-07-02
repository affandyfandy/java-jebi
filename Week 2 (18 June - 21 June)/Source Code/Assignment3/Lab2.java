import java.util.InputMismatchException;
import java.util.Scanner;

// Custom exception for menu selection
class InvalidMenuSelectionException extends RuntimeException {
    public InvalidMenuSelectionException(String message) {
        super(message);
    }
}

public class Lab2 {

    public static void main(String[] args) {
        // Define an array of fruit options
        String[] fruits = {"Option 1: Apple", "Option 2: Banana", "Option 3: Cherry", "Option 4: Orange", "Option 5: Elderberry"};
        
        // Use try-with-resources to ensure the Scanner is closed automatically
        try (Scanner input = new Scanner(System.in)) {
            System.out.println("\n\nEnter a number between 0 and 4 to select a fruit:");

            // Capture the user's selection
            int selection = input.nextInt();

            // Validate the selection
            if (selection < 0 || selection >= fruits.length) {
                throw new InvalidMenuSelectionException("Invalid selection. Please choose a number between 0 and 4.");
            }

            // Display the selected fruit
            System.out.println("You selected: " + fruits[selection]);

        } catch (InputMismatchException e) {
            // Handle non-integer input
            System.out.println("Error: Please enter a valid integer.");
        } catch (InvalidMenuSelectionException e) {
            // Handle out-of-bounds selection
            System.out.println("Error: " + e.getMessage());
        }
    }
}
