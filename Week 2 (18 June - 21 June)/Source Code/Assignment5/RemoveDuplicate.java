
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RemoveDuplicate {

    public static void main(String[] args) {
        String inputFile = "data.csv";
        String outputFile = "output.csv";
        String keyField = "id"; // Assuming 'id' is the key field

        try {
            removeDuplicates(inputFile, outputFile, keyField);
            System.out.println("Duplicates removed successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeDuplicates(String inputFile, String outputFile, String keyField) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        FileWriter writer = new FileWriter(outputFile);

        Map<String, String> uniqueEntries = new HashMap<>();
        String line;

        // Read header and write to output file
        if ((line = reader.readLine()) != null) {
            writer.write(line + System.lineSeparator());
        }

        // Process each line of the input file
        while ((line = reader.readLine()) != null) {
            String[] fields = line.split(",");
            if (fields.length > 0) {
                String key = fields[0]; // Assuming key field is the first column

                // Check if this key is already present (to remove duplicates)
                if (!uniqueEntries.containsKey(key)) {
                    uniqueEntries.put(key, line);
                    writer.write(line + System.lineSeparator());
                }
            }
        }

        reader.close();
        writer.close();
    }
}