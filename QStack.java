import java.util.function.Consumer;

public class QStack {
    private final int max;
    private int size;
    private Node head;

    public QStack(int max) { this.max = max; }

    public static void main(String[] args) {
        QStack stack = new QStack(Integer.MAX_VALUE);
        for (char ch = '0'; ch <= '9'; ch++) {
            stack.push(ch);
        }
        stack.forEach(System.out::println);
    }

    // O(1)
    public void clear() {
        head = null;  size = 0;
    }
    // O(1)
    public char top() { // <- wrap in java.util.Optional ?
        Node h = head;
        if (h == null) throw new NullPointerException();
        return h.item;
    }
    // O(1)
    public char pop() {
        Node h = head;
        char element = h.item;
        Node next = h.next;
        h.next = null;
        head = next;
        size--;
        return element;
    }
    // O(1)
    public void push(char e) {
        checkForConstrained();
        final Node h = head;
        head = new Node(e, h);
        size++;
    }

    // O(1)
    public boolean isEmpty() {return size < 1; }

    // O(1)
    public int size() { return size; }


    void forEach(Consumer<Character> consumer) {
        for (Node h = head; h != null; h = h.next) {
            consumer.accept(h.item);
        }
    }

    final void checkForConstrained() {
        if (size >= max) throw new RuntimeException(
                "stack capacity does not allow adding more elements than: "+max
        );
    }



    private static final class Node {
        char item;
        Node next;

        Node(char item, Node next) {
            this.item = item;
            this.next = next;
        }
    }

}