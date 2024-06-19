### 4.1 - Explain about DeadLock and give example? How to prevent
Deadlock occurs in a multithreaded environment when two or more threads are blocked indefinitely, each waiting for a resource that the other thread(s) holds. This situation arises due to a set of conditions being met simultaneously:
- **Mutual Exclusion:** At least one resource must be held in a non-shareable mode, meaning only one thread can use it at a time.

- **Hold and Wait:** A thread holding a resource is waiting to acquire additional resources held by other threads.

- **No Preemption:** Resources cannot be forcibly taken away from threads that hold them; they must be voluntarily released.

- **Circular Wait:** A set of threads, each holding one resource and waiting to acquire a resource held by another thread in the set, forms a circular chain of dependencies.
#
`Example of Deadlock`

Consider a scenario with two threads and two resources (resource1 and resource2), where each thread acquires one resource and then tries to acquire the other:

```java
class Resource {
    public final String name;

    public Resource(String name) {
        this.name = name;
    }

    public void acquire(Resource other) {
        synchronized (this) {
            System.out.println(Thread.currentThread().getName() + ": Locked " + this.name);

            try {
                Thread.sleep(100); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (other) {
                System.out.println(Thread.currentThread().getName() + ": Locked " + other.name);
            }
        }
    }
}

public class DeadlockExample {
    public static void main(String[] args) {
        final Resource resource1 = new Resource("Resource 1");
        final Resource resource2 = new Resource("Resource 2");

        Thread t1 = new Thread(() -> {
            resource1.acquire(resource2);
        });

        Thread t2 = new Thread(() -> {
            resource2.acquire(resource1);
        });

        t1.start();
        t2.start();
    }
}

```
#
### Execution Flow :

Two instances of the Resource class, resource1 and resource2, are created with distinct names ("Resource 1" and "Resource 2" respectively).

**Thread t1 Execution:**
    
- Thread t1 starts execution by acquiring resource1 first using the acquire method defined within the Resource class. This method locks resource1 and then attempts to acquire resource2.
- During this process, t1 holds the lock on resource1 while it waits to acquire resource2.

**Thread t2 Execution:**

- Simultaneously, Thread t2 also starts and tries to acquire resource2 first using the acquire method. It successfully locks resource2 and then attempts to acquire resource1.
- t2 holds the lock on resource2 while it waits to acquire resource1.

**Deadlock Occurrence:**

- At this point, t1 holds resource1 and waits for resource2, which is held by t2.
- Similarly, t2 holds resource2 and waits for resource1, which is held by t1.
- Both threads are now stuck in a circular waiting state where each thread is holding one resource and waiting indefinitely for the other resource to be released. This situation is known as deadlock.

#
### **Deadlock Prevention Strategies**
To avoid deadlocks, consider these effective strategies:

- **Lock Ordering:** Always acquire locks in a consistent order across all threads. This strategy ensures that threads cannot enter a deadlock situation because they all follow the same lock acquisition sequence.

- **Lock Timeout:** Implement lock timeouts to prevent threads from waiting indefinitely. Threads attempt to acquire a lock with a timeout duration, and if unsuccessful within that time, they can take alternative actions rather than waiting indefinitely.

- **Avoidance of Nested Locks:** Minimize the use of nested locks whenever possible. Nested locks increase the complexity of deadlock scenarios because they require careful management of lock acquisition and release sequences.

- **Using tryLock:** Instead of using synchronized blocks, utilize java.util.concurrent.locks.Lock interface methods like tryLock(). This method attempts to acquire the lock but returns immediately if the lock is not available, allowing threads to perform other tasks or handle failures gracefully.

- **Deadlock Detection and Recovery:** Implement algorithms to detect potential deadlocks dynamically during runtime. Once detected, recovery mechanisms such as thread interruption or releasing resources can resolve the deadlock.

- **Higher-Level Concurrency Abstractions:** Utilize higher-level concurrency utilities provided by Java's java.util.concurrent package, such as Semaphore, CountDownLatch, and CyclicBarrier. These abstractions simplify concurrent programming and reduce the likelihood of deadlocks.

#
### 4.2 - Creates a bank account with concurrent deposits and withdrawals using threads.
`BankAccount`
```java
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    private double balance;
    private final Lock lock = new ReentrantLock(); // ReentrantLock for thread safety

    // Deposit method
    public void deposit(double amount) {
        lock.lock(); // Acquire the lock
        try {
            balance += amount; // Update balance
            System.out.println("Deposited: " + amount + ", Balance: " + balance);
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    // Withdraw method
    public void withdraw(double amount) {
        lock.lock(); // Acquire the lock
        try {
            if (balance >= amount) { // Check for sufficient balance
                balance -= amount; // Update balance
                System.out.println("Withdrawn: " + amount + ", Balance: " + balance);
            } else {
                System.out.println("Insufficient balance for withdrawal: " + amount);
            }
        } finally {
            lock.unlock(); // Release the lock
        }
    }
}

```
```java
public class BankAccountDemo {
    public static void main(String[] args) {
        BankAccount account = new BankAccount(); 

        // Deposit task
        Runnable depositTask = () -> {
            for (int i = 0; i < 10; i++) {
                account.deposit(100); // Deposit $100 ten times
                try { 
                    Thread.sleep(100); 
                } catch (InterruptedException e) {}
            }
        };

        // Withdraw task
        Runnable withdrawTask = () -> {
            for (int i = 0; i < 10; i++) {
                account.withdraw(50); // Withdraw $50 ten times
                try { 
                    Thread.sleep(100); 
                } catch (InterruptedException e) {}
            }
        };

        // Create threads
        Thread depositThread = new Thread(depositTask);
        Thread withdrawThread = new Thread(withdrawTask);

        // Start threads
        depositThread.start();
        withdrawThread.start();
    }
}

```
#
### 4.3 - Write a Java program that sorts an array of integers using multiple threads.
The MultiThreadedSort program sorts an array of integers using multiple threads. It divides the array into segments and assigns each segment to a thread for sorting concurrently using an ExecutorService.
```java
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

```
### Explanationn
`Initialization (main method)`

- Generates a random array of integers using generateRandomArray(size).
- Prints the original array using Arrays.toString(array).

`ExecutorService Setup`

- Creates a fixed thread pool (executor) with THREAD_COUNT threads using Executors.newFixedThreadPool(THREAD_COUNT).

`Segmentation and Task Assignment`

- Calculates segmentLength as array.length / THREAD_COUNT to divide the array evenly among the threads.
- Iterates over THREAD_COUNT threads, creating SortTask instances for each segment of the array:
    
    - Calculates start and end indices for each segment.
    - Creates a new SortTask(array, start, end) and submits it to executor for execution using executor.execute().

`Sorting Task (SortTask class)`

- Implements Runnable interface for concurrent execution.
- Receives the array (array) and segment boundaries (start and end) in its constructor.
- Inside run() method, sorts the specified segment of the array using Arrays.sort(array, start, end + 1).

`Executor Shutdown and Wait`

- Initiates executor.shutdown() to prevent new tasks from being submitted.
- Waits for all tasks to complete with executor.awaitTermination(1, TimeUnit.MINUTES), allowing up to one minute for completion.

`Output`

- Once all threads complete sorting, prints the sorted array using Arrays.toString(array).

#
Each segment of the array is sorted independently by a separate thread (SortTask). This approach improves sorting performance for large arrays by utilizing multiple CPU cores effectively. The use of ExecutorService simplifies thread management and allows for efficient distribution of tasks across threads, achieving parallelism in array sorting.
#
### 4.4 - What are noticeable things when using multiple thread?
- **Concurrency and Parallelism**: Threads allow for concurrent execution of tasks, meaning multiple operations can happen simultaneously. This can lead to faster execution times and better utilization of multi-core processors, resulting in parallelism.

- **Thread Interference**: Since threads share resources such as memory and variables, care must be taken to synchronize access to shared data. Without proper synchronization, concurrent threads accessing and modifying shared data can lead to unpredictable behavior, including data corruption or inconsistent results.

- **Race Conditions**: Race conditions occur when the outcome of the program depends on the relative timing of threads. If threads access shared resources in an unpredictable order, the program might produce incorrect results. Proper synchronization techniques (like locks or atomic operations) are necessary to prevent race conditions.

- **Deadlocks**: A deadlock happens when two or more threads are blocked forever, waiting for each other to release resources. Deadlocks can occur if threads acquire resources in different orders and hold them while waiting for others, preventing progress. Careful design and use of techniques like resource ordering and timeouts can mitigate deadlocks.

- **Thread Scheduling**: Threads are scheduled by the operating system, which determines when each thread gets CPU time. This scheduling can affect the overall performance of the application, especially when many threads are contending for resources.

- **Context Switching Overhead**: Context switching refers to the process of saving and restoring the state of a thread so that it can resume execution from the same point later. Switching between threads incurs overhead due to saving and restoring thread states, which can impact performance, especially if threads are frequently interrupted.

- **Increased Complexity**: Multi-threaded programs can be more complex to design, implement, and debug compared to single-threaded programs. Issues such as race conditions, deadlocks, and synchronization bugs require careful attention and thorough testing to identify and resolve.

- **Improved Responsiveness**: In applications where responsiveness is crucial (e.g., user interfaces), multi-threading can improve responsiveness by allowing tasks to run concurrently. For example, background tasks can execute independently while the main thread handles user input.

- **Resource Utilization**: Multi-threading can improve resource utilization by keeping the CPU busy with useful work. However, excessive use of threads without proper design can lead to resource contention and inefficient use of system resources.

- **Debugging and Testing Challenges**: Debugging multi-threaded programs can be challenging due to the non-deterministic nature of thread execution. Issues may be difficult to reproduce consistently, and bugs related to thread synchronization can be subtle and hard to detect.


#
### 4.5 - Illustrate the usage of the ReadWriteLock interface for concurrent read-write access to a shared resource.

The ReadWriteLock interface in Java provides a pair of associated locks, one for read-only operations and one for write operations. The ReadWriteLock allows multiple threads to read a shared resource concurrently but restricts access for write operations to only one thread at a time. This mechanism is useful for scenarios where the resource is read frequently but written infrequently, allowing improved concurrency and performance.
#
`Example`

Simple example where a shared List<Integer> is accessed by multiple threads. Readers will be able to read concurrently, while writers will have exclusive access for modifications.
```java
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockExample {
    private final List<Integer> sharedResource = new ArrayList<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    // Method for reading the shared resource
    public void readResource() {
        lock.readLock().lock(); // Acquire the read lock
        try {
            System.out.println(Thread.currentThread().getName() + " Reading: " + sharedResource);
        } finally {
            lock.readLock().unlock(); // Release the read lock
        }
    }

    // Method for writing to the shared resource
    public void writeResource(int value) {
        lock.writeLock().lock(); // Acquire the write lock
        try {
            sharedResource.add(value);
            System.out.println(Thread.currentThread().getName() + " Writing: " + value);
        } finally {
            lock.writeLock().unlock(); // Release the write lock
        }
    }

    public static void main(String[] args) {
        ReadWriteLockExample example = new ReadWriteLockExample();

        // Create threads for reading
        Runnable readTask = () -> {
            for (int i = 0; i < 5; i++) {
                example.readResource();
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        };

        // Create threads for writing
        Runnable writeTask = () -> {
            for (int i = 0; i < 5; i++) {
                example.writeResource(i);
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        };

        // Start threads
        Thread reader1 = new Thread(readTask, "Reader1");
        Thread reader2 = new Thread(readTask, "Reader2");
        Thread writer1 = new Thread(writeTask, "Writer1");

        reader1.start();
        reader2.start();
        writer1.start();
    }
}
```
#
### Explanation
`Shared Resource and Lock Initialization`

- sharedResource is a list that will be accessed by multiple threads.
- lock is an instance of ReentrantReadWriteLock which provides both a read and a write lock.

`Reading from the Shared Resource`

- readResource method acquires the read lock using lock.readLock().lock().
- The thread reads the sharedResource and prints the current state.
- The read lock is released using lock.readLock().unlock().

`Writing to the Shared Resource`

- writeResource method acquires the write lock using lock.writeLock().lock().
- The thread writes a new value to the sharedResource.
- The write lock is released using lock.writeLock().unlock().

`Thread Creation and Execution`

- readTask and writeTask are Runnable tasks that define what each thread should do.
- Thread instances (reader1, reader2, and writer1) are created and started, each performing their respective tasks.
- Readers (reader1 and reader2) run the readTask which reads the shared resource multiple times.
- The writer (writer1) runs the writeTask which writes to the shared resource multiple times.

`Concurrency Control`

- Multiple reader threads can acquire the read lock simultaneously, allowing concurrent reads.
- Only one writer thread can acquire the write lock, ensuring exclusive access for writing.
- The ReadWriteLock ensures that while a write operation is happening, no read operations can occur and vice versa.


#
### Key Points
- **Read-Write Locking:** The ReadWriteLock interface facilitates multiple concurrent reads but restricts writes to a single thread at a time.
- **Improved Performance:** This model improves performance in read-heavy applications by reducing contention for the shared resource.
- **Thread Safety:** Ensures that shared data is safely accessed and modified, preventing data inconsistency and corruption.