import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class Main {
    public static void main(String[] args) {
        String inputFilePath = "input.txt";
        String folderPath;
        String[] searchInputs;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            folderPath = reader.readLine();
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            searchInputs = sb.toString().trim().split("\n");
        } catch (IOException e) {
            System.out.println("Error reading input file: " + e.getMessage());
            return;
        }

        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        String fileCount = (files!=null ? String.valueOf(files.length) :"0");
        System.out.println(fileCount + " files found.");

        if (files != null) {
            for (String searchInput : searchInputs) {
                StringBuilder output = new StringBuilder();
                output.append("\"").append(searchInput).append("\" found in: ");

                for (File file : files) {
                    if (file.isFile() && file.getName().toLowerCase().endsWith(".pdf")) {
                        if (searchFile(file, searchInput)) {
                            output.append("\"").append(file.getName()).append("\", ");
                        }
                    }
                }

                // Remove the trailing comma and space
                if (output.length() > 0) {
                    output.setLength(output.length() - 2);
                }

                System.out.println(output.toString());
            }
        }
    }

    private static boolean searchFile(File file, String searchInput) {
        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            return text.contains(searchInput);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
