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


#
### 4.3 - Write a Java program that sorts an array of integers using multiple threads.


#
### 4.4 - What are noticeable things when using multiple thread?



#
### 4.5 - Illustrate the usage of the ReadWriteLock interface for concurrent read-write access to a shared resource.
