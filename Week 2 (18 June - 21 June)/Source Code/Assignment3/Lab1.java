import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Lab1 {

    /**
     * Reads the entire content of a specified file.
     *
     * @param path The path to the file to read.
     * @return The file content as a string.
     * @throws IOException If an I/O error occurs.
     */
    private static String loadFile(String path) throws IOException {
        StringBuilder fileContent = new StringBuilder();

        // Using try-with-resources to ensure BufferedReader is closed after use
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append(System.lineSeparator());
            }
        }

        return fileContent.toString().trim(); // Trim to remove the last line separator
    }

    /**
     * Writes the given content to a specified file.
     *
     * @param path    The path to the file to write.
     * @param content The content to be written to the file.
     * @throws IOException If an I/O error occurs.
     */
    private static void saveToFile(String path, String content) throws IOException {
        // Using try-with-resources to ensure BufferedWriter is closed after use
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(content);
        }
    }

    public static void main(String[] args) {
        String inputFile = "test1.txt";
        String outputFile = "test2.txt";

        // Attempt to read from the input file and write to the output file
        try {
            String data = loadFile(inputFile);
            if (data.isEmpty()) {
                System.out.println("The file at " + inputFile + " is empty.");
            } else {
                System.out.println("Contents of " + inputFile + ":");
                System.out.println(data);
                saveToFile(outputFile, data);
            }
        } catch (IOException e) {
            if (!Files.exists(Paths.get(inputFile))) {
                System.out.println("Error: The file " + inputFile + " does not exist.");
            } else {
                System.out.println("Error: An I/O issue occurred while handling the files.");
            }
        }
    }
}
