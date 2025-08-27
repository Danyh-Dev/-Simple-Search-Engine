package javaapplication12;

public class InvertedIndex {
    private LinkedList<InvertedIndexEntry> index;

    public InvertedIndex() {
        this.index = new LinkedList<>();
    }

    public void addWord(String word, int documentId) {
        // Check if the word already exists in the index
        for (int i = 0; i < index.size(); i++) {
            InvertedIndexEntry entry = index.get(i);
            if (entry.getWord().equals(word)) {
                entry.addDocumentId(documentId);
                return;
            }
        }
        
        // If the word is not found, create a new entry and add it
        InvertedIndexEntry newEntry = new InvertedIndexEntry(word);
        newEntry.addDocumentId(documentId);
        index.add(newEntry);
    }

    public InvertedIndexEntry findEntry(String word) {
        for (int i = 0; i < index.size(); i++) {
            InvertedIndexEntry entry = index.get(i);
            if (entry.getWord().equals(word)) {
                return entry;
            }
        }
        return null;
    }

    public LinkedList<InvertedIndexEntry> getIndex() {
        return index;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < index.size(); i++) {
            sb.append(index.get(i).toString()).append("\n");
        }
        return sb.toString();
    }
}