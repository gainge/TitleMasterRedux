package main;

import java.util.Collection;

public class HeapQueue<T extends Comparable> implements PriorityQueue<T> {

    /* Data Members */
    private int size;
    private Comparable[] heap; // I guess we have to use an array of comparable here, huh?


    public HeapQueue(Collection<T> objects) {
        // Initialize our queue with these objects
        this(objects.size());

        for (T object : objects) {
            this.add(object);
        }
    }

    public HeapQueue(int maxCapacity) {
        this.size = maxCapacity;

        this.heap = new Comparable[maxCapacity];
    }


    @Override
    public void add(T obj) {
        heap[size] = obj;

        bubbleUp(size);

        size++;
    }

    @Override
    public T peek() {
        return (T) heap[0];
    }

    @Override
    public T poll() {
        Comparable head = heap[0];

        if (size > 1) {
            // Now we have to move the last element up to the top
            heap[0] = heap[size - 1];
            bubbleDown(0);
        }

        // Decrease the size
        size--;

        return (T) head;
    }

    @Override
    public int size() {
        return this.size;
    }


    /* Helper Methods */
    private void swapIndices(int index1, int index2) {
        Comparable temp = heap[index1];
        heap[index1] = heap[index2];
        heap[index2] = temp;
    }

    private void bubbleUp(int childIndex) {
        if (childIndex == 0) return; // don't need to bubble up the root

        // Find the parent
        int parentIndex = (childIndex - 1) / 2;
        // If needed, swap and recurse
        if (heap[childIndex].compareTo(heap[parentIndex]) < 0) {
            swapIndices(childIndex, parentIndex);
            bubbleUp(parentIndex);
        }
    }

    private void bubbleDown(int parentIndex) {
        // Swap with the min child, if available
        int minChild = getMinChild(parentIndex);

        if (minChild < 0) return;   // We're done here!

        if (heap[parentIndex].compareTo(heap[minChild]) > 0) { // If parent is greater than child
            // Otherwise, perform the swap
            swapIndices(parentIndex, minChild);
            bubbleDown(minChild);
        }

    }


    /**
     * Returns the index of the minimum child of the node specified by index
     * @param index the parent from whom to branch off of, getting the child
     * @return the index of the child of the node at index with the lowest value, -1 if index is a leaf node
     */
    private int getMinChild(int index) {
        int leftChildIndex = getLeftChild(index);
        int rightChildIndex = getRightChild(index);

        if (leftChildIndex >= size || rightChildIndex >= size) return -1;

        Comparable leftChild = heap[leftChildIndex];
        Comparable rightChild = heap[rightChildIndex];

        if (leftChild.compareTo(rightChild) < 0) {
            return leftChildIndex;
        } else {
            return rightChildIndex;
        }
    }

    private int getLeftChild(int index) {
        return (index * 2) + 1;
    }

    private int getRightChild(int index) {
        return (index + 1) * 2;
    }

}
