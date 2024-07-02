import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultiThreadedSort {
    private static final int THREAD_COUNT = 4;

    public static void main(String[] args) throws InterruptedException {
        int[] array = generateRandomArray(20); // Generate random array
        System.out.println("Original array: " + Arrays.toString(array));

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        // Divide the array into equal parts for each thread
        int segmentLength = array.length / THREAD_COUNT;
        for (int i = 0; i < THREAD_COUNT; i++) {
            int start = i * segmentLength;
            int end = (i == THREAD_COUNT - 1) ? array.length - 1 : (start + segmentLength - 1);
            executor.execute(new SortTask(array, start, end));
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println("Sorted array: " + Arrays.toString(array));
    }

    // Helper method to generate an array of random integers
    private static int[] generateRandomArray(int size) {
        int[] array = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(100); // Generate random integers between 0 and 99
        }
        return array;
    }

    // Runnable task to sort a segment of the array
    private static class SortTask implements Runnable {
        private final int[] array;
        private final int start, end;

        public SortTask(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            Arrays.sort(array, start, end + 1); // Sort the segment of the array
        }
    }
}
