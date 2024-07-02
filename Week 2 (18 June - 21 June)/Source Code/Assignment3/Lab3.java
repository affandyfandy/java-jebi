import java.util.Scanner;

// Custom exception class for when no vowels are found
class NoVowelsException extends Exception {
    public NoVowelsException(String message) {
        super(message);
    }
}

public class Lab3 {

    // Method to check if a string contains vowels
    public static void checkForVowels(String input) throws NoVowelsException {
        boolean foundVowel = false;
        // Check each character in the string
        for (char c : input.toCharArray()) {
            if (isVowel(c)) {
                foundVowel = true;
                break;
            }
        }
        
        // If no vowel found, throw NoVowelsException
        if (!foundVowel) {
            throw new NoVowelsException("No vowels found in the input string.");
        }
    }

    // Helper method to check if a character is a vowel
    private static boolean isVowel(char c) {
        return "AEIOUaeiou".indexOf(c) != -1;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("\n\nEnter a string: ");
            String userInput = scanner.nextLine();

            checkForVowels(userInput);
            System.out.println("Input has vowels.");
        } catch (NoVowelsException e) {
            System.out.println(e.getMessage());
        } finally {
            scanner.close(); 
        }
    }
}
