package javaapplication12;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVReader {
    
    public LinkedList<String> loadStopWords(String filePath) {
        LinkedList<String> stopWords = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Trem and convert to lowercase 
                stopWords.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stopWords;
    }

    public LinkedList<LinkedList<String>> readCSV(String filePath, int rowsToRead, LinkedList<String> stopWords) {
        javaapplication12.LinkedList<javaapplication12.LinkedList<String>> documents = new javaapplication12.LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int rowIndex = 0;

            // Skip heeder 
            for (int i = 0; i < 2; i++)
                br.readLine();

            while ((line = br.readLine()) != null && rowIndex < rowsToRead) {
                // Split the line 
                String[] words = line.trim().split("[,\\s]+");

                // Create a linked list for the current document
                javaapplication12.LinkedList<String> document = new javaapplication12.LinkedList<>();

              
                for (int i = 1; i < words.length; i++) {
                    String word = words[i].trim()
                            .replaceAll("^\"|\"$", "")
                            .toLowerCase()
                            .replaceAll("[\\.,']", "")
                            .replaceAll("\\s+", " ");

                    // Add word to document only if it's not a stop word and not empty
                    if (!stopWords.contains(word) && !word.isEmpty()) {
                        document.add(word);
                    }
                }

                // Add the document list to documents list
                documents.add(document);
                rowIndex++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return documents;
    }
}