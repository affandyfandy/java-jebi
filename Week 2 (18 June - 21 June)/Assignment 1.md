#### Week 2 (Assignment 1)
#
### 1. Design Class with states and behaviors
```java
public class Dog {
    // States
    private String color;
    private String name;
    private String breed;

    // Behaviors
    public void wagTail() {
        System.out.println(name + " is wagging the tail.");
    }
    public void bark() {
        System.out.println(name + " is barking.");
    }
    public void eat() {
        System.out.println(name + " is eating.");
    }
    // Constructor
    public Dog(String color, String name, String breed) {
        this.color = color;
        this.name = name;
        this.breed = breed;
    }

    public static void main(String[] args) {
        Dog myDog = new Dog("Brown", "Buddy", "Labrador");

        // Accessing attributes 
        System.out.println("My dog's name is " + myDog.name);
        System.out.println("My dog's color is " + myDog.color);
        System.out.println("My dog's breed is " + myDog.breed);

        // Calling methods from Dog object
        myDog.wagTail();
        myDog.bark();
        myDog.eat();
    }
}
```
This Java code defines a class named Dog that serves as a blueprint for creating dog objects, each with specific characteristics and behaviors. The class includes three private attributes: color, name, and breed, which represent the dog's properties and ensure encapsulation by restricting direct access from outside the class. The Dog class provides a constructor that initializes these attributes when a new dog object is created. This constructor uses the this keyword to differentiate between the class attributes and the parameters passed during object instantiation. Additionally, the class includes three public methods: wagTail(), bark(), and eat(), each of which outputs a message describing the corresponding behavior of the dog, incorporating the dog's name for personalized output.

#
### 2. Design Class Teacher and Subject



#
### 3. Design Class Teacher and Subject (Array)
