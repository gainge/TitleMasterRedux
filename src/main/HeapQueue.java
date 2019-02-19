package main;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class HeapQueue<T extends Comparable> implements PriorityQueue<T> {

    /* Data Members */
    private int size;
    private int maxCapacity;
    private Comparable[] heap; // I guess we have to use an array of comparable here, huh?


    public HeapQueue(Collection<T> objects) {
        if (objects == null) throw new IllegalArgumentException("Supplied collection was null!");
        // Initialize our queue with these objects
        int capacity = objects.size();
        if (capacity <= 0) throw new IllegalArgumentException("Collection size must be greater than zero!");
        this.maxCapacity = capacity;
        this.size = 0;

        this.heap = new Comparable[maxCapacity];

        for (T object : objects) {
            this.add(object);
        }
    }

    public HeapQueue(int maxCapacity) {
        if (maxCapacity <= 0) throw new IllegalArgumentException("Capacity must be greater than zero!");
        this.maxCapacity = maxCapacity;
        this.size = 0;

        this.heap = new Comparable[maxCapacity];
    }


    @Override
    public void add(T obj) {
        if (obj == null) throw new IllegalArgumentException("Object to add was null!");
        if (size == maxCapacity) {
            // We should double our size!
            maxCapacity *= 2;
            heap = Arrays.copyOf(heap, maxCapacity);
        }

        // Put our dude in at the end
        heap[size] = obj;
        // Only to quickly bubble him up to the right place :)
        bubbleUp(size);

        size++;
    }

    @Override
    public T peek() {
        if (size == 0) return null;
        return (T) heap[0];
    }

    @Override
    public T poll() {
        if (size == 0) return null;
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




    // The big three


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        str.append("Elements: {");

        for (int i = 0; i < size; i++) {
            str.append(heap[i].toString());

            if (i < size - 1) {
                str.append(", ");
            }
        }

        str.append("}");

        return str.toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HeapQueue)) return false;
        HeapQueue<?> heapQueue = (HeapQueue<?>) o;

        // Check the easy stuff first
        if (!(size == heapQueue.size && maxCapacity == heapQueue.maxCapacity)) {
            return false;
        }

        // Check all of our children
        for (int i = 0; i < size; i++) {
            if (!(heap[i].equals(heapQueue.heap[i]))) {
                return false;
            }
        }
        // Whatever remains, however unlikely, must be the truth
        return true;
    }

    @Override
    public int hashCode() {
        int fullHash = Objects.hash(size, maxCapacity);;

        if (size > 0) {
            fullHash *= heap[0].hashCode();         // First element
            fullHash *= heap[size - 1].hashCode();  // Last element
        }

        fullHash *= 41;                             // Prime numberino

        return fullHash;
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

        if (rightChildIndex >= size) {      // Right child is unswappable
            if (leftChildIndex >= size) {   // Left child is also unswappable (nonexistent)
                return -1;                  // This dude is a leaf node
            } else {
                return leftChildIndex;      // Hey, we actually do have a left child!
            }
        }

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
