package javaapplication12;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String csvFile = "/Users/mohammedalgumlas/Desktop/CSC212dataset.csv"; // documnets file path
        String stopWordsFile = "/Users/mohammedalgumlas/Downloads/data/stop.txt";    //stop words file path
        int rowsToRead = 50;// Number of rows to read

        //CSVReader to load data
        CSVReader csvReader = new CSVReader();
        LinkedList<String> stopWords = csvReader.loadStopWords(stopWordsFile);
        LinkedList<LinkedList<String>> documents = csvReader.readCSV(csvFile, rowsToRead, stopWords);

        //inverted index 
        InvertedIndex invertedIndex = new InvertedIndex();
        for (int docId = 0; docId < documents.size(); docId++) {
            LinkedList<String> document = documents.get(docId);
            for (int i = 0; i < document.size(); i++) {
                String word = document.get(i).toLowerCase(); // Convert to lowercace
                invertedIndex.addWord(word, docId);
            }
        }

        // BST inverted INdex
        InvertedIndexBST invertedIndexBST = new InvertedIndexBST();
        for (int docId = 0; docId < documents.size(); docId++) {
            LinkedList<String> document = documents.get(docId);
            for (int i = 0; i < document.size(); i++) {
                String word = document.get(i).toLowerCase(); // Convert to lowercase for consistency
                invertedIndexBST.addWord(word, docId);
            }
        }

        //  QueryProcessor And Ranking
        QueryProcessor queryProcessor = new QueryProcessor(invertedIndexBST);
        Ranking ranking = new Ranking(invertedIndex, documents.size());

        // Menu
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Search Engine System ---");
            System.out.println("1. Retrieve a term");
            System.out.println("2. Boolean retrieval (AND/OR)");
            System.out.println("3. Ranked retrieval");
            System.out.println("4. Indexed documents");
            System.out.println("5. Indexed tokens");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // clear garbage

            if (choice == 1) {
                System.out.println("\nChoose search method:");
                System.out.println("1. Using index with lists");
                System.out.println("2. Using inverted index with lists");
                System.out.println("3. Using inverted index with BST");
                System.out.print("Enter choice (1-3): ");
                int searchMethod = scanner.nextInt();
                scanner.nextLine(); // clear garbage

                System.out.print("Enter term to search: ");
                String term = scanner.nextLine().toLowerCase(); 

                if (!stopWords.contains(term)) {
                    switch (searchMethod) {
                        case 1:
                            // retrive terms with regular index
                            System.out.println("Documents containing '" + term + "':");
                            for (int docId = 0; docId < documents.size(); docId++) {
                                LinkedList<String> document = documents.get(docId);
                                if (document.contains(term)) {
                                    System.out.println("Document " + docId);
                                }
                            }
                            break;
                        case 2:
                            // retrive terms with inverted index with lists
                            System.out.println("Documents containing '" + term + "' (using inverted index):");
                            InvertedIndexEntry entry = invertedIndex.findEntry(term);
                            if (entry != null) {
                                System.out.println("Document IDs and frequencies:");
                                LinkedList<DocumentFrequency> docFreqs = entry.getDocumentFrequencies();
                                for (int i = 0; i < docFreqs.size(); i++) {
                                    DocumentFrequency df = docFreqs.get(i);
                                    System.out.println("Doc " + df.documentId + ": frequency = " + df.frequency);
                                }
                            } else {
                                System.out.println("Term not found");
                            }
                            break;
                        case 3:
                            // retrive terms with inverted index BST
                            System.out.println("Documents containing '" + term + "' (using BST):");
                            LinkedList<Integer> bstResults = invertedIndexBST.searchWord(term);
                            if (bstResults != null && bstResults.size() > 0) {
                                System.out.println("Document IDs: " + bstResults);
                            } else {
                                System.out.println("Term not found");
                            }
                            break;
                    }
                } else {
                    System.out.println("'" + term + "' is a stop word and has been excluded from the index.");
                }

            } else if (choice == 2) {
                System.out.println("\nBoolean Retrieval (Note: Stop words are excluded)");
                System.out.print("Enter Boolean query (e.g., 'term1 AND term2' or 'term1 OR term2'): ");
                String query = scanner.nextLine().toLowerCase(); // Convert to lowercase 
                LinkedList<Integer> results = queryProcessor.processQuery(query);
                if (results != null && results.size() > 0) {
                    System.out.println("Matching documents: " + results);
                } else {
                    System.out.println("No matching documents found");
                }

            } else if (choice == 3) {
                System.out.println("\nRanked Retrieval");
                System.out.print("Enter query for ranking: ");
                String query = scanner.nextLine().toLowerCase(); // Convert to lowercase 
                
                //in case users enters stopwords, Remove stop words from query
                StringBuilder cleanQuery = new StringBuilder();
                String[] terms = query.split("\\s+");
                for (String term : terms) {
                    if (!stopWords.contains(term)) {
                        if (cleanQuery.length() > 0) {
                            cleanQuery.append(" ");
                        }
                        cleanQuery.append(term);
                    }
                }
                
                if (cleanQuery.length() > 0) {
                    LinkedList<DocumentScore> rankedResults = ranking.rankDocuments(cleanQuery.toString());
                    if (rankedResults != null && rankedResults.size() > 0) {
                        System.out.println("\nRanked results \n(DocID\tScore):");
                        for (int i = 0; i < rankedResults.size(); i++) {
                            System.out.println(rankedResults.get(i));
                        }
                    } else {
                        System.out.println("No matching documents found");
                    }
                } else {
                    System.out.println("Query contains only stop words. Please try again with more specific terms.");
                }

            } else if (choice == 4) {
                System.out.println("\nIndexed Documents:");
                System.out.println("DocID\tNumber of Tokens");
                for (int docId = 0; docId < documents.size(); docId++) {
                    LinkedList<String> document = documents.get(docId);
                    System.out.println(docId + "\t" + document.size());
                }

            } else if (choice == 5) {
                System.out.println("\nIndexed Tokens:\n");
                System.out.println("Word" + String.format("%-20s", "") + "Number of Documents");
                System.out.println("-".repeat(45));
                LinkedList<InvertedIndexEntry> entries = invertedIndex.getIndex();
                for (int i = 0; i < entries.size(); i++) {
                    InvertedIndexEntry entry = entries.get(i);
                    LinkedList<DocumentFrequency> docFreqs = entry.getDocumentFrequencies();
                    System.out.printf("%-30s%d%n", entry.getWord(), docFreqs.size());
                }

            } else if (choice == 6) {
                System.out.println("Exiting...");
                break;

            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }
}
