import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BankAccountDemo {
    public static void main(String[] args) {
        BankAccount account = new BankAccount(); // Create a shared BankAccount instance

        // ExecutorService to manage threads
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Submit deposit task
        for (int i = 0; i < 5; i++) { // Deposit $200 five times
            executor.submit(() -> {
                account.deposit(200);
                try {
                    Thread.sleep(200); // Adding slight delay
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        // Submit withdraw task
        for (int i = 0; i < 5; i++) { // Withdraw $100 five times
            executor.submit(() -> {
                account.withdraw(100);
                try {
                    Thread.sleep(200); // Adding slight delay
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        // Shutdown executor
        executor.shutdown();
    }
}
