### Assignment 5
Implement thread-safe Singleton, and explain

A Singleton is a design pattern used in programming to ensure that a class has only one instance and provides a global point of access to that instance. The Thread-Safe Singleton extends this concept by making sure that this single instance remains consistent and unique even when multiple threads are trying to access or create it simultaneously.

In a multi-threaded environment, without proper synchronization, multiple threads could create multiple instances of the Singleton class, which defeats the purpose of having a Singleton. A thread-safe Singleton uses synchronization mechanisms to ensure that only one thread can create or access the Singleton instance at a time.

#
## Implementing Thread-Safe Singleton
#### **Synchronized Method**
This approach uses the synchronized keyword to ensure that only one thread can access the method to create the instance at any given time.

This method Ensures that only one instance is created even when multiple threads access the method simultaneously. But Synchronized method can cause performance bottlenecks because it locks the method every time it’s called, even if the instance has already been created.
```java
public class ThreadSafeSingleton {
    private static ThreadSafeSingleton instance;
    private ThreadSafeSingleton() {
    }

    public static synchronized ThreadSafeSingleton getInstance() {
        if (instance == null) {
            instance = new ThreadSafeSingleton();
        }
        return instance;
    }
}
```
getInstance() Method method is synchronized, which means only one thread can execute it at a time. This ensures that if multiple threads are calling getInstance() simultaneously and the instance is not yet created (instance == null), only one thread will create it while others wait.
#
#### Double-Checked Locking
This method reduces the overhead of synchronization by checking if the instance is null before entering the synchronized block, and again within the block before creating the instance.
```java
public class ThreadSafeSingleton {

    private static volatile ThreadSafeSingleton instance;

    private ThreadSafeSingleton() {
    }

    public static ThreadSafeSingleton getInstance() {
        if (instance == null) { // First check
            synchronized (ThreadSafeSingleton.class) {
                if (instance == null) { // Second check
                    instance = new ThreadSafeSingleton();
                }
            }
        }
        return instance;
    }
}
```

This method More efficient than the synchronized method because it only locks the critical section of code when the instance is null. But Slightly more complex and requires careful handling of the volatile keyword to prevent issues with the instance variable being cached incorrectly by the threads.

**Volatile Keyword**: Ensures that the instance variable is always read from main memory, not from thread cache, avoiding issues where different threads might see different values for instance.

**Double-Checked Locking**: This pattern first checks if instance is null without locking (to avoid the cost of synchronization if unnecessary), then enters a synchronized block only if instance is still null. This way, only one thread creates the instance while others wait, improving performance compared to the synchronized method.
#
#### Bill Pugh Singleton Design
This approach uses a static inner class to hold the Singleton instance. The instance is created when the static inner class is loaded, which is thread-safe and ensures lazy initialization.
```java
public class ThreadSafeSingleton {
    private ThreadSafeSingleton() {
    }
    private static class SingletonHelper {
        private static final ThreadSafeSingleton INSTANCE = new ThreadSafeSingleton();
    }

    public static ThreadSafeSingleton getInstance() {
        return SingletonHelper.INSTANCE;
    }
}
```
**Static Inner Class**: The SingletonHelper class is only loaded once, and the INSTANCE field is initialized statically when the class is loaded by the JVM. This is guaranteed to be thread-safe by the JVM.

**Lazy Initialization**: The Singleton instance is created only when getInstance() is called for the first time. This ensures efficiency and thread safety without needing to use synchronization.

This method Efficient and simple, without the need for synchronization. Ensures lazy initialization and thread safety by leveraging the class loading mechanism. But None significant, this method is generally recommended for most use cases.

