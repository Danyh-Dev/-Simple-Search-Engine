
package javaapplication12;


public class InvertedIndexBSTNode {
    String word;
    LinkedList<Integer> documentIds;
    InvertedIndexBSTNode left, right;

    public InvertedIndexBSTNode(String word) {
        this.word = word;
        this.documentIds = new LinkedList<>();
        this.left = this.right = null;
    }

    public void addDocumentId(int documentId) {
        if (!documentIds.contains(documentId)) {
            documentIds.add(documentId);
        }
    }
}