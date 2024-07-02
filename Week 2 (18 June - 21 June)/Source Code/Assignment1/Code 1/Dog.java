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
