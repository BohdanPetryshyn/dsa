import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    public Node<Item> first = null;
    public Node<Item> last = null;
    public int size = 0;

    // construct an empty deque
    public Deque() {
    }

    // is the deque empty?
    public boolean isEmpty() {
        return this.size() == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item can't be null.");
        }

        Node<Item> oldFirst = this.first;

        this.first = new Node<>(item, oldFirst, null);

        if (this.isEmpty()) {
            this.last = this.first;
        } else {
            oldFirst.prev = this.first;
        }

        this.size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item can't be null.");
        }

        Node<Item> oldLast = this.last;

        this.last = new Node<>(item, null, oldLast);

        if (this.isEmpty()) {
            this.first = this.last;
        } else {
            oldLast.next = this.last;
        }

        this.size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("The deque is empty.");
        }

        Node<Item> oldFirst = this.first;

        this.first = oldFirst.next;

        if (this.size() == 1) {
            this.last = null;
        } else {
            this.first.prev = null;
        }

        this.size--;

        return oldFirst.value;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("The deque is empty.");
        }

        Node<Item> oldLast = this.last;

        this.last = oldLast.prev;

        if (this.size() == 1) {
            this.first = null;
        } else {
            this.last.next = null;
        }

        this.size--;

        return oldLast.value;

    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private static class Node<Item> {
        public final Item value;
        public Node<Item> next;
        public Node<Item> prev;

        public Node(Item value, Node<Item> next, Node<Item> prev) {
            this.value = value;
            this.next = next;
            this.prev = prev;
        }
    }

    private class DequeIterator implements Iterator<Item> {
        private Node<Item> next = first;

        @Override
        public boolean hasNext() {
            return this.next != null;
        }

        @Override
        public Item next() {
            if (isEmpty()) {
                throw new NoSuchElementException("The deque is empty.");
            }

            Node<Item> oldNext = this.next;
            this.next = oldNext.next;

            return oldNext.value;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported;");
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();

        assert deque.isEmpty();
        assert deque.size() == 0;

        deque.addFirst(2);
        deque.addFirst(3);
        deque.addLast(1);

        assert deque.removeFirst() == 3;
        assert deque.removeLast() == 1;

        assert !deque.isEmpty();
        assert deque.size() == 1;

        assert deque.removeFirst() == 2;

        assert deque.isEmpty();
        assert deque.size() == 0;

        deque.addFirst(1);
        deque.addFirst(2);
        deque.addFirst(3);

        int expectedFirst = 3;

        for (int item : deque) {
            assert item == expectedFirst;

            expectedFirst--;
        }
    }

}