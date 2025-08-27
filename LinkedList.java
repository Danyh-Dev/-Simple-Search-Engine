
package javaapplication12;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class LinkedList<T> {
    private Node<T> head;
    private Node<T> current;
     private int size;

    public LinkedList() {
        head = current = null;
                size = 0;

    }

    public boolean empty() {
        return head == null;
    }

    public boolean last() {
        return current != null && current.next == null;
    }

     public int size() {
        return size;
    }

           public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    public boolean contains(T element) {
        Node<T> current = head;
        while (current != null) {
            if (element == null && current.data == null) {
                return true;
            }
            if (element != null && element.equals(current.data)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public Node<T> getHead() {
        return head;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        Node<T> current = head;
        while (current != null) {
            result.append(current.data);
            if (current.next != null) {
                result.append(", ");
            }
            current = current.next;
        }
        result.append("]");
        return result.toString();
    }
    

}
