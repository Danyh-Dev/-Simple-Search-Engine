package javaapplication12;

public class InvertedIndexEntry {
    private String word;
    private LinkedList<DocumentFrequency> documentFrequencies;

    public InvertedIndexEntry(String word) {
        this.word = word;
        this.documentFrequencies = new LinkedList<>();
    }

    public String getWord() {
        return word;
    }

    public LinkedList<DocumentFrequency> getDocumentFrequencies() {
        return documentFrequencies;
    }

    public LinkedList<Integer> getDocumentIds() {
        LinkedList<Integer> docIds = new LinkedList<>();
        for (int i = 0; i < documentFrequencies.size(); i++) {
            docIds.add(documentFrequencies.get(i).documentId);
        }
        return docIds;
    }

    public void addDocumentId(int documentId) {
        // Check if document already exists
        for (int i = 0; i < documentFrequencies.size(); i++) {
            DocumentFrequency df = documentFrequencies.get(i);
            if (df.documentId == documentId) {
                df.frequency++;
                return;
            }
        }
        // If not found, add new entry
        documentFrequencies.add(new DocumentFrequency(documentId));
    }

    public int getTermFrequency(int documentId) {
        for (int i = 0; i < documentFrequencies.size(); i++) {
            DocumentFrequency df = documentFrequencies.get(i);
            if (df.documentId == documentId) {
                return df.frequency;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(word + ": ");
        for (int i = 0; i < documentFrequencies.size(); i++) {
            if (i > 0) sb.append(", ");
            DocumentFrequency df = documentFrequencies.get(i);
            sb.append("(Doc:" + df.documentId + ", Freq:" + df.frequency + ")");
        }
        return sb.toString();
    }
}

//  class to store document ID and term frequency together
class DocumentFrequency {
    int documentId;
    int frequency;

    public DocumentFrequency(int documentId) {
        this.documentId = documentId;
        this.frequency = 1;
    }
}




 

  
