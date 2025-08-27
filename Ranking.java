package javaapplication12;


public class Ranking {
    private InvertedIndex invertedIndex;
    private int numDocuments;

    public Ranking(InvertedIndex invertedIndex, int numDocuments) {
        this.invertedIndex = invertedIndex;
        this.numDocuments = numDocuments;
    }


    public LinkedList<DocumentScore> rankDocuments(String query) {
        String[] terms = query.toLowerCase().split("\\s+");
        LinkedList<DocumentScore> scores = new LinkedList<>();
        
        // Initialize score array for all documents
        int[] documentScores = new int[numDocuments];
        
        // Calculate scores using term frequencies
        for (String term : terms) {
            InvertedIndexEntry entry = invertedIndex.findEntry(term);
            if (entry != null) {
                LinkedList<DocumentFrequency> docFreqs = entry.getDocumentFrequencies();
                for (int i = 0; i < docFreqs.size(); i++) {
                    DocumentFrequency df = docFreqs.get(i);
                    documentScores[df.documentId] += df.frequency;
                }
            }
        }
        
        // Create DocumentScore objects for documents that score do not equal zero
        for (int docId = 0; docId < numDocuments; docId++) {
            if (documentScores[docId] > 0) {
                scores.add(new DocumentScore(docId, documentScores[docId]));
            }
        }

        // Sort by score in descending order
        sortScoresDescending(scores);
        return scores;
    }

 
    private void sortScoresDescending(LinkedList<DocumentScore> scores) {
        // Using bubble sort 
        for (int i = 0; i < scores.size() - 1; i++) {
            for (int j = 0; j < scores.size() - i - 1; j++) {
                DocumentScore current = scores.get(j);
                DocumentScore next = scores.get(j + 1);
                if (current.getScore() < next.getScore()) {
                    // Swap scores
                    DocumentScore temp = new DocumentScore(current.getDocumentId(), current.getScore());
                    current.setData(next);
                    next.setData(temp);
                }
            }
        }
    }
}

class DocumentScore {
    private int documentId;
    private int score;

    public DocumentScore(int documentId, int score) {
        this.documentId = documentId;
        this.score = score;
    }

    public int getDocumentId() {
        return documentId;
    }

    public int getScore() {
        return score;
    }

    public void setData(DocumentScore other) {
        this.documentId = other.documentId;
        this.score = other.score;
    }

    @Override
    public String toString() {
        return documentId + "\t" + score;
    }
}
