package main.queue;

import java.util.Collection;

/**
 * Priority Queue interface.  Uses generics and requires that the type parameter be comparable
 * @param <T> The type of data which this queue will hold
 */
public interface PriorityQueue<T extends Comparable> {


    /**
     * Adds the provided object to the priority queue, given that the configured size will not be exceeded
     * @param obj The object to be added to the priority queue
     */
    void add(T obj);

    /**
     * Returns the first element in the queue without modifying the state of the queue.  Returns null if the queue is empty
     * @return The first element of the queue, null if hte queue is empty
     */
    T peek();

    /**
     * Returns the first element of the queue and removes it from the queue.  Returns null if the queue is empty.
     * @return The first element of the queue, null if the queue is empty.
     */
    T poll();

    /**
     * Returns the current size of the queue in terms of the number of elements.
     * @return The number of elements in the queue.  0 <= size <= max capacity
     */
    int size();







}
