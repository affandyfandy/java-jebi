### Assignment 2
How oop principles perform in java?
#
Object-Oriented Programming (OOP) principles in Java are implemented through classes and objects. The main principles of OOP in Java are encapsulation, inheritance, polymorphism, and abstraction. OOP principles in Java enable developers to create robust, maintainable, and scalable software systems by organizing code into classes and objects that interact with each other.

#
### Encapsulation
Encapsulation ensures that the internal state of an object is hidden and can only be accessed through public methods. This helps to protect the object's internal state from external interference and misuse.  

```java
class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        balance = initialBalance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        balance -= amount;
    }

    public double getBalance() {
        return balance;
    }
}

public class Encapsulation {
    public static void main(String[] args) {
        BankAccount account = new BankAccount(1000);
        account.deposit(500);
        System.out.println("Balance: " + account.getBalance()); 
    }
}


```
#
### Inheritance
Inheritance allows one class to inherit properties and behavior from another. This enables code reuse and facilitates the creation of a hierarchy of classes.

```java
class Animal {
    public void sound() {
        System.out.println("The animal makes a sound");
    }
}

class Dog extends Animal {
    @Override
    public void sound() {
        System.out.println("The dog barks");
    }
}

public class Inheritance {
    public static void main(String[] args) {
        Dog myDog = new Dog();
        myDog.sound(); 
    }
}
```
#
### Polymorphism
Polymorphism enables objects to be treated as instances of their parent class. This allows for more flexibility in programming and enables methods to be overridden or overloaded.
```java
class Shape {
    public void draw() {
        System.out.println("Drawing a shape");
    }
}

class Circle extends Shape {
    @Override
    public void draw() {
        System.out.println("Drawing a circle");
    }
}

public class Polymorphism {
    public static void main(String[] args) {
        Shape shape = new Circle();
        shape.draw(); 
    }
}
```

#
### Abstraction
Abstraction focuses on hiding the implementation details and showing only the necessary information to the outside world. This helps to simplify complex systems and improve modularity.

```java
interface Printable {
    void print();
}

class Document implements Printable {
    @Override
    public void print() {
        System.out.println("Printing a document");
    }
}

class Image implements Printable {
    @Override
    public void print() {
        System.out.println("Printing an image");
    }
}

public class Abstraction {
    public static void main(String[] args) {
        Printable document = new Document();
        document.print(); 

        Printable image = new Image();
        image.print(); 
    }
}
```