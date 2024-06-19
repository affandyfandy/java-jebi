public class Main {
    public static void main(String[] args) {
        Account savingAccount1 = new SavingAccount("SA1001");
        Account.isValidAccountId("SA1001");
        savingAccount1.deposit(1000);
        savingAccount1.withdraw(200);
        savingAccount1.log("Transaction completed.");
        savingAccount1.printBalance();
        
        Account savingAccount2 = new SavingAccount("SA1002");
        Account.isValidAccountId("SA1002");
        savingAccount2.deposit(1500);
        savingAccount2.withdraw(300);
        savingAccount2.log("Transaction completed.");
        savingAccount2.printBalance();
        
        Account currentAccount1 = new CurrentAccount("CA2001");
        Account.isValidAccountId("CA2001");
        currentAccount1.deposit(2000);
        currentAccount1.withdraw(2500); // Within overdraft limit
        currentAccount1.log("Transaction completed.");
        currentAccount1.printBalance();
        
        Account currentAccount2 = new CurrentAccount("CA2002");
        Account.isValidAccountId("CA2002");
        currentAccount2.deposit(500);
        currentAccount2.withdraw(1200); // Overdraft limit exceeded
        currentAccount2.log("Transaction attempted and failed.");
        currentAccount2.printBalance();
    }
}