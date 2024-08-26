import java.util.Iterator;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private static int MIN_LENGTH = 2;

    @SuppressWarnings("unchecked")
    private Item[] array = (Item[]) new Object[MIN_LENGTH];
    private int last = 0;
    private int free = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return last - free;

    }

    // add the item
    public void enqueue(Item item) {
        if (this.last == this.array.length) {
            this.resize();
        }

        array[this.last] = item;

        last++;
    }

    // remove and return a random item
    public Item dequeue() {
        int randomIndex = this.getRandomItemIndex();

        Item randomItem = this.array[randomIndex];
        this.array[randomIndex] = null;

        this.free++;

        if (this.size() <= this.array.length / 4) {
            this.resize();
        }

        return randomItem;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        int randomIndex = this.getRandomItemIndex();

        return this.array[randomIndex];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private int getRandomItemIndex() {
        while (true) {
            int randomIndex = StdRandom.uniformInt(last);
            if (this.array[randomIndex] != null) {
                return randomIndex;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Item[] oldArray = this.array;
        int newLength = this.size() == 0 ? MIN_LENGTH : this.size() * 2;
        this.array = (Item[]) new Object[newLength];

        this.last = 0;
        this.free = 0;

        for (int i = 0; i < oldArray.length; i++) {
            if (oldArray[i] != null) {
                this.array[last] = oldArray[i];
                last++;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private class RandomizedQueueIterator implements Iterator<Item> {
        private final Item[] arrayCopy = (Item[]) new Object[size()];
        private final int[] permutation = StdRandom.permutation(size());
        private int next = 0;

        {
            for (int i = 0, j = 0; i < array.length; i++) {
                if (array[i] != null) {
                    this.arrayCopy[j] = array[i];
                    j++;
                }
            }
        }

        @Override
        public boolean hasNext() {
            return next < size();
        }

        @Override
        public Item next() {
            int nextIndex = this.permutation[next];
            next++;
            return arrayCopy[nextIndex];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported;");
        }

    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();

        assert queue.size() == 0;
        assert queue.isEmpty();

        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        assert queue.size() == 3;
        assert !queue.isEmpty();

        assert queue.sample() != null;

        int item1 = queue.dequeue();
        int item2 = queue.dequeue();
        int item3 = queue.dequeue();

        assert item1 != item2;
        assert item2 != item3;
        assert item3 != item1;

        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        for (Integer item : queue) {
            assert item != null;
        }
    }
}