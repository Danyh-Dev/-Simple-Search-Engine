package javaapplication12;


public class QueryProcessor {

    private InvertedIndexBST invertedIndexBST;

    public QueryProcessor(InvertedIndexBST invertedIndexBST) {
        this.invertedIndexBST = invertedIndexBST;
    }

    public LinkedList<Integer> processQuery(String query) {
        // Stack for operands (document ID lists) and operators
        Stack<LinkedList<Integer>> operandStack = new Stack<>();
        Stack<String> operatorStack = new Stack<>();

        // Tokenize the query into words and operators
        String[] tokens = query.split("\\s+");

        for (String token : tokens) {
            if (token.equalsIgnoreCase("AND") || token.equalsIgnoreCase("OR")) {
                // Handle operator precedence
                while (!operatorStack.isEmpty() && precedence(operatorStack.peek()) >= precedence(token)) {
                    processOperator(operatorStack, operandStack);
                }
                operatorStack.push(token);
            } else {
                // For words, fetch document IDs from the inverted index
                LinkedList<Integer> docIds = invertedIndexBST.searchWord(token.toLowerCase());
                if (docIds == null) {
                    docIds = new LinkedList<>(); // Empty list if the word is not found
                }
                operandStack.push(docIds);
            }
        }

        // Process any remaining operators in the stack
        while (!operatorStack.isEmpty()) {
            processOperator(operatorStack, operandStack);
        }

        // Final result in the operand stack
        return operandStack.isEmpty() ? new LinkedList<>() : operandStack.pop();
    }

    // Determine the precedence of the operators
    private int precedence(String operator) {
        if (operator.equalsIgnoreCase("AND")) {
            return 2; // AND has higher precedence than Or
        } else if (operator.equalsIgnoreCase("OR")) {
            return 1; // OR has lower precedence 
        }
        return 0;
    }

    // Perform the operation between two operands
    private void processOperator(Stack<String> operatorStack, Stack<LinkedList<Integer>> operandStack) {
        String operator = operatorStack.pop();
        LinkedList<Integer> rightOperand = operandStack.pop();
        LinkedList<Integer> leftOperand = operandStack.pop();

        if (operator.equalsIgnoreCase("AND")) {
            operandStack.push(performIntersection(leftOperand, rightOperand));
        } else if (operator.equalsIgnoreCase("OR")) {
            operandStack.push(performUnion(leftOperand, rightOperand));
        }
    }

    // Intersection of two linked lists (AND operation)
    private LinkedList<Integer> performIntersection(LinkedList<Integer> list1, LinkedList<Integer> list2) {
        LinkedList<Integer> result = new LinkedList<>();
        Node<Integer> current1 = list1.getHead();
        Node<Integer> current2 = list2.getHead();

        while (current1 != null && current2 != null) {
            if (current1.data.equals(current2.data)) {
                result.add(current1.data);
                current1 = current1.next;
                current2 = current2.next;
            } else if (current1.data < current2.data) {
                current1 = current1.next;
            } else {
                current2 = current2.next;
            }
        }
        return result;
    }

    // Union of two linked lists (OR operation)
    private LinkedList<Integer> performUnion(LinkedList<Integer> list1, LinkedList<Integer> list2) {
        LinkedList<Integer> result = new LinkedList<>();
        Node<Integer> current1 = list1.getHead();
        Node<Integer> current2 = list2.getHead();

        while (current1 != null || current2 != null) {
            if (current1 == null) {
                result.add(current2.data);
                current2 = current2.next;
            } else if (current2 == null) {
                result.add(current1.data);
                current1 = current1.next;
            } else if (current1.data.equals(current2.data)) {
                result.add(current1.data);
                current1 = current1.next;
                current2 = current2.next;
            } else if (current1.data < current2.data) {
                result.add(current1.data);
                current1 = current1.next;
            } else {
                result.add(current2.data);
                current2 = current2.next;
            }
        }
        return result;
    }
}
