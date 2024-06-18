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
```java
public class Subject {
    // States
    private String name;
    private String classId;

    // Constructor for creating Subject with name
    public Subject(String name) {
        this.name = name;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getClassId() {
        return classId;
    }

    // Setter for classId
    public void setClassId(String classId) {
        this.classId = classId;
    }
}
```
 Subject class represents a subject that can be taught. It has two properties: name and classId. The name represents the subject's name, while classId indicates the class or group the subject is associated with. The class includes a constructor to initialize the name, getters to access name and classId, and a setter to update the classId.
```java
public class Teacher {
    // States
    private String name;
    private int age;
    private String subject;

    // Constructor for creating teacher with name and age
    public Teacher(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Constructor for creating teacher with subject
    public Teacher(String name, String subject) {
        this.name = name;
        this.subject = subject;
    }

    // Behavior: teaching
    public void teach(Subject subject) {
        System.out.println("Teacher " + name + " teaching " + subject.getName() + " for Class " + subject.getClassId());
    }
}
```
 Teacher class represents a teacher. It has three properties: name, age, and subject, where subject is the subject they are teaching. The class provides two constructors: one to create a teacher with a name and age, and another to create a teacher with a name and a specific subject. It also includes a teach method, which takes a Subject object as an argument and prints a message indicating that the teacher is teaching that particular subject for a specific class.
```java
public class Main {
    public static void main(String[] args) {
        // Creating a Teacher with name and age
        Teacher tamTeacher = new Teacher("Tam", 35);

        // Creating a Subject with name
        Subject mathSubject = new Subject("Mathematics");
        mathSubject.setClassId("1");

        // Teaching the subject
        tamTeacher.teach(mathSubject);
    }
}
```
In Main class it creates a Teacher object named "Tam" who is 35 years old, and a Subject object representing "Mathematics". The Subject object is assigned to class "1". The program then simulates Tam teaching Mathematics to class 1 by calling the teach method on the Teacher object with the Subject object as an argument, resulting in a printed message describing the teaching activity.
#
### 3. Design Class Teacher and Subject (Array)
```java
public class Student {
    // States
    private String name;
    private int age;
    private Subject[] subjects; // Array to hold subjects the student is learning

    // Constructor for creating student with name and age
    public Student(String name, int age) {
        this.name = name;
        this.age = age;
        this.subjects = new Subject[5]; // Assuming a student can learn up to 5 subjects initially
    }

    // Method for learning a subject
    public void learn(Subject subject) {
        for (int i = 0; i < subjects.length; i++) {
            if (subjects[i] == null) {
                subjects[i] = subject;
                System.out.println(name + " is learning " + subject.getName() + " for Class " + subject.getClassId());
                return;
            }
        }
        System.out.println(name + " cannot learn more subjects. Array is full.");
    }
}
```
```java
public class Subject {
    // States
    private String name;
    private String classId;

    // Constructor for creating Subject with name and classId
    public Subject(String name, String classId) {
        this.name = name;
        this.classId = classId;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getClassId() {
        return classId;
    }
}
```
```java
public class Teacher {
    // States
    private String name;
    private int age;
    private Subject subject;

    // Constructor for creating teacher with name and age
    public Teacher(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Method for teaching a subject
    public void teach(Subject subject) {
        this.subject = subject;
        System.out.println("Teacher " + name + " is teaching " + subject.getName() + " for Class " + subject.getClassId());
    }
}
```
```java
public class Main {
    public static void main(String[] args) {
        // Create a Teacher
        Teacher tamTeacher = new Teacher("Tam", 35);

        // Create Subjects
        Subject mathSubject = new Subject("Mathematics", "1");
        Subject scienceSubject = new Subject("Science", "2");

        // Create a Student
        Student johnStudent = new Student("John", 15);

        // Teacher teaches Mathematics
        tamTeacher.teach(mathSubject);

        // Student John learns Mathematics and Science
        johnStudent.learn(mathSubject);
        johnStudent.learn(scienceSubject);
    }
}
```