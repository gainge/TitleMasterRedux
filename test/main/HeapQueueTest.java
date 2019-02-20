package main;

import main.queue.HeapQueue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HeapQueueTest {

    private HeapQueue<Integer> heapQueue;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        heapQueue = null;
    }



    //region poll tests
    @Test
    @DisplayName("Test Poll Empty Queue")
    public void testPollEmptyQueue() {
        heapQueue = new HeapQueue<>(10);

        Integer expected = null;
        Integer actual = heapQueue.poll();

        assertEquals(expected, actual);
        assertTrue(heapQueue.size() == 0);
    }

    @Test
    @DisplayName("Test Poll size 1")
    public void testPollSize1Queue() {
        heapQueue = new HeapQueue<>(10);
        heapQueue.add(10);

        Integer expected = 10;

        assertTrue(heapQueue.size() == 1);
        Integer actual = heapQueue.poll();

        assertEquals(expected, actual);
        assertTrue(heapQueue.size() == 0);
    }

    @Test
    @DisplayName("Test poll size > 1")
    public void testPollMediumSizeQueue() {
        heapQueue = new HeapQueue<>(10);
        heapQueue.add(10);
        heapQueue.add(5);
        heapQueue.add(2);
        heapQueue.add(3);
        heapQueue.add(11);

        Integer expected = 2;

        assertTrue(heapQueue.size() == 5);
        Integer actual = heapQueue.poll();

        assertEquals(expected, actual);
        assertTrue(heapQueue.size() == 4);
    }

    @Test
    @DisplayName("Test consecutive polls medium queue")
    public void testConsecutivePollMediumSizeQueue() {
        heapQueue = new HeapQueue<>(10);
        heapQueue.add(10);
        heapQueue.add(5);
        heapQueue.add(2);
        heapQueue.add(3);
        heapQueue.add(11);

        List<Integer> expected = Arrays.asList(2, 3, 5, 10);
        List<Integer> actual = new ArrayList<>();

        assertTrue(heapQueue.size() == 5);

        for (int i = 0; i < expected.size(); i++) {
            actual.add(heapQueue.poll());
            assertTrue(heapQueue.size() == 5 - i - 1);
        }

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test Poll to empty then poll")
    public void testPollToDepleteThenPoll() {
        heapQueue = new HeapQueue<>(5);
        heapQueue.add(3);

        Integer expected = null;
        heapQueue.poll();

        assertTrue(heapQueue.size() == 0);
        Integer actual = heapQueue.poll();

        assertEquals(expected, actual);
        assertTrue(heapQueue.size() == 0);
    }


    @Test
    @DisplayName("Test Poll max size queue")
    public void testPollMaxSizeQueue() {
        heapQueue = new HeapQueue<>(3);
        heapQueue.add(3);
        heapQueue.add(2);
        heapQueue.add(1);

        Integer expected = 1;

        assertTrue(heapQueue.size() == 3);
        Integer actual = heapQueue.poll();

        assertEquals(expected, actual);
        assertTrue(heapQueue.size() == 2);
    }

    @Test
    @DisplayName("Test Poll Deplete full Queue")
    public void testPollDepleteQueue() {
        heapQueue = new HeapQueue<>(5);
        heapQueue.add(10);
        heapQueue.add(5);
        heapQueue.add(2);
        heapQueue.add(3);
        heapQueue.add(11);

        List<Integer> expected = Arrays.asList(2, 3, 5, 10, 11, null, null);
        List<Integer> actual = new ArrayList<>();

        assertTrue(heapQueue.size() == 5);

        for (int i = 0; i < expected.size(); i++) {
            actual.add(heapQueue.poll());
            assertTrue(heapQueue.size() == ((i > 4) ? 0 : 5 - i - 1));
        }

        assertEquals(expected, actual);
    }

    //endregion



    //region peek tests

    @Test
    @DisplayName("Test Peek empty queue")
    public void testPeekEmptyQueue() {
        heapQueue = new HeapQueue<>(10);

        Integer expected = null;
        Integer actual = heapQueue.peek();

        assertEquals(expected, actual);
        assertTrue(heapQueue.size() == 0);
    }

    @Test
    @DisplayName("Test Peek Queue size == 1")
    public void testPeekSize1Queue() {
        heapQueue = new HeapQueue<>(10);
        heapQueue.add(7);

        Integer expected = 7;
        Integer actual = heapQueue.peek();

        assertEquals(expected, actual);
        assertTrue(heapQueue.size() == 1);
    }

    @Test
    @DisplayName("Test Consecutive peek size == 1")
    public void testConsecutivePeekSize1Queue() {
        heapQueue = new HeapQueue<>(10);
        heapQueue.add(7);

        Integer expected = 7;
        Integer actual = heapQueue.peek();

        assertEquals(expected, actual);

        actual = heapQueue.peek();
        assertEquals(expected, actual);
        assertTrue(heapQueue.size() == 1);
    }

    @Test
    @DisplayName("Test many peeks size == 1")
    public void testManyPeekSize1Queue() {
        heapQueue = new HeapQueue<>(10);
        heapQueue.add(7);

        Integer expected = 7;

        for (int i = 0; i < 10; i++) {
            Integer actual = heapQueue.peek();
            assertEquals(expected, actual);
            assertTrue(heapQueue.size() == 1);
        }

    }


    @Test
    @DisplayName("Test peek medium size queue")
    public void testPeekMediumSizeQueue() {
        heapQueue = new HeapQueue<>(10);
        heapQueue.add(10);
        heapQueue.add(5);
        heapQueue.add(2);
        heapQueue.add(3);
        heapQueue.add(11);

        Integer expected = 2;
        Integer actual = heapQueue.peek();

        assertEquals(expected, actual);
        assertTrue(heapQueue.size() == 5);
    }

    @Test
    @DisplayName("Test consecutive peek medium size queue")
    public void testConsecutivePeekMediumSizeQueue() {
        heapQueue = new HeapQueue<>(10);
        heapQueue.add(10);
        heapQueue.add(5);
        heapQueue.add(2);
        heapQueue.add(3);
        heapQueue.add(11);

        Integer expected = 2;

        for (int i = 0; i < 10; i++) {
            Integer actual = heapQueue.peek();
            assertEquals(expected, actual);
            assertTrue(heapQueue.size() == 5);
        }
    }

    @Test
    @DisplayName("Test peek full queue")
    public void testPeekFullQueue() {
        heapQueue = new HeapQueue<>(3);
        heapQueue.add(5);
        heapQueue.add(2);
        heapQueue.add(3);

        Integer expected = 2;
        Integer actual = heapQueue.peek();

        assertEquals(expected, actual);
        assertTrue(heapQueue.size() == 3);
    }

    @Test
    @DisplayName("Test consecutive peek full queue")
    public void testConsecutivePeekFullQueue() {
        heapQueue = new HeapQueue<>(3);
        heapQueue.add(5);
        heapQueue.add(2);
        heapQueue.add(3);

        Integer expected = 2;

        for (int i = 0; i < 10; i++) {
            Integer actual = heapQueue.peek();
            assertEquals(expected, actual);
            assertTrue(heapQueue.size() == 3);
        }

    }

    //endregion


    //region add tests

    @Test
    @DisplayName("Test add to empty queue")
    public void testAddToEmptyQueue() {
        heapQueue = new HeapQueue<>(10);

        assertTrue(heapQueue.size() == 0);
        heapQueue.add(3);
        assertTrue(heapQueue.size() == 1);
        assertTrue(heapQueue.peek() == 3);
    }

    @Test
    @DisplayName("Test add to non-empty queue")
    public void testAddToNonEmptyQueue() {
        heapQueue = new HeapQueue<>(10);
        heapQueue.add(3);
        heapQueue.add(30);
        heapQueue.add(8);

        assertTrue(heapQueue.size() == 3);
        heapQueue.add(2);
        assertTrue(heapQueue.size() == 4);
        assertTrue(heapQueue.peek() == 2);
    }

    @Test
    @DisplayName("Test add to capacity - 1 queue")
    public void testAddToNearFullQueue() {
        heapQueue = new HeapQueue<>(4);
        heapQueue.add(4);
        heapQueue.add(3);
        heapQueue.add(2);

        assertTrue(heapQueue.size() == 3);
        heapQueue.add(1);
        assertTrue(heapQueue.size() == 4);
        assertTrue(heapQueue.peek() == 1);
    }

    @Test
    @DisplayName("Test add to already full queue")
    public void testAddToFullQueue() {
        heapQueue = new HeapQueue<>(1);
        heapQueue.add(100);

        assertTrue(heapQueue.size() == 1);
        assertTrue(heapQueue.peek() == 100);

        heapQueue.add(1);

        assertTrue(heapQueue.size() == 2);
        assertTrue(heapQueue.peek() == 1);
    }

    @Test
    @DisplayName("Test add null object")
    public void testAddNullObject() {
        heapQueue = new HeapQueue<>(10);

        assertTrue(heapQueue.size() == 0);

        assertThrows(RuntimeException.class, () -> heapQueue.add(null));

        assertTrue(heapQueue.size() == 0);
    }


    @Test
    @DisplayName("Test consecutive additions")
    public void testConsecutiveAdditions() {
        int queueCapacity = 5;
        heapQueue = new HeapQueue<>(queueCapacity);

        List<Integer> numbersToAdd = Arrays.asList(7, 2, 4, 1, 5, 10, 1, 13);

        List<Integer> expectedPeeks = Arrays.asList(7, 2, 2, 1, 1, 1, 1, 1);
        List<Integer> expectedBeforeSizes = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
        List<Integer> expectedAfterSizes = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);


        for (int i = 0; i < numbersToAdd.size(); i++) {
            assertTrue(heapQueue.size() == expectedBeforeSizes.get(i));

            heapQueue.add(numbersToAdd.get(i));
            assertEquals(expectedPeeks.get(i), heapQueue.peek());

            assertTrue(heapQueue.size() == expectedAfterSizes.get(i));
        }
    }

    //endregion



    //region size tests

    @Test
    @DisplayName("Test size empty queue")
    public void testSizeEmptyQueue() {
        heapQueue = new HeapQueue<>(10);

        assertTrue(heapQueue.size() == 0);
    }

    @Test
    @DisplayName("Test size queue 1 element")
    public void testSize1ElementQueue() {
        heapQueue = new HeapQueue<>(10);
        heapQueue.add(40);

        assertTrue(heapQueue.size() == 1);
    }

    @Test
    @DisplayName("Test size max capacity")
    public void testSizeMaxCapacity() {
        heapQueue = new HeapQueue<>(3);
        heapQueue.add(1);
        heapQueue.add(14);
        heapQueue.add(41);

        assertTrue(heapQueue.size() == 3);

    }

    @Test
    @DisplayName("Test size empty after poll")
    public void testSizeEmptyAfterPollQueue() {
        heapQueue = new HeapQueue<>(10);
        heapQueue.add(4);

        assertTrue(heapQueue.size() == 1);
        heapQueue.poll();
        assertTrue(heapQueue.size() == 0);
    }

    @Test
    @DisplayName("Test size after over capacity addition")
    public void testSizeAfterFailedAddition() {
        heapQueue = new HeapQueue<>(3);
        heapQueue.add(1);
        heapQueue.add(14);
        heapQueue.add(41);

        assertTrue(heapQueue.size() == 3);
        heapQueue.add(6);
        assertTrue(heapQueue.size() == 4);
    }

    @Test
    @DisplayName("Test size after invalid addition")
    public void testSizeAfterInvalidAddition() {
        heapQueue = new HeapQueue<>(3);
        heapQueue.add(1);
        heapQueue.add(14);
        heapQueue.add(41);

        assertTrue(heapQueue.size() == 3);
        assertThrows(RuntimeException.class, () -> heapQueue.add(null));
        assertTrue(heapQueue.size() == 3);
    }

    //endregion


    //region constructor tests

    @Test
    @DisplayName("Test with valid max capacity")
    public void testValidMaxCapacity() {
        heapQueue = new HeapQueue<>(4);

        assertNotNull(heapQueue);
        assertTrue(heapQueue.size() == 0);
    }


    @Test
    @DisplayName("Test valid collection construction")
    public void testValidCollectionConstruction() {
        Collection<Integer> initialItems = Arrays.asList(5, 1, 2, 4, 3, 6);
        heapQueue = new HeapQueue<>(initialItems);

        assertTrue(heapQueue.size() == 6);
        assertTrue(heapQueue.peek() == 1);
    }

    @Test
    @DisplayName("Test invalid max capacity construction")
    public void testInvalidMaxCapacityConstruction() {
        assertThrows(RuntimeException.class, () -> heapQueue = new HeapQueue<>(0));
    }

    @Test
    @DisplayName("Test negative max capacity construction")
    public void testNegativeMaxCapacityConstruction() {
        assertThrows(RuntimeException.class, () -> heapQueue = new HeapQueue<>(-10));
    }

    @Test
    @DisplayName("Test null Collection construction")
    public void testNullCollectionConstruction() {
        assertThrows(RuntimeException.class, () -> heapQueue = new HeapQueue<>(null));
    }

    //endregion


}