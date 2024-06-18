#### Week 2 (Assignment 1)
#
### 2.1 - What happen if implement 2 interface have same default method? How to solve? Demo in code.
When implementing multiple interfaces in Java, if both interfaces have a default method with the same name and signature, a conflict arises. This is because the implementing class cannot decide which default method to inherit. Java provides a way to resolve this conflict by explicitly specifying which default method to use or by overriding the default method in the implementing class

If MyClass implements both FirstInterface and SecondInterface, and both interfaces have a default method named log, Java cannot determine which log method to use. This ambiguity leads to a compilation error.

To resolve this, we must override the default method in the implementing class and explicitly specify which interface's default method we want to call using the `InterfaceName.super.methodName` syntax.

```java
public interface FirstInterface {
    void firstMethod(String string);

    default void log(String string) {
        System.out.println("This method is default implementation from FirstInterface: " + string);
    }
}

public interface SecondInterface {
    void secondMethod();

    default void log(String string) {
        System.out.println("This method is default implementation from SecondInterface: " + string);
    }
}

public class MyClass implements FirstInterface, SecondInterface {
    @Override
    public void firstMethod(String string) {
        System.out.println("First method: " + string);
    }

    @Override
    public void secondMethod() {
        System.out.println("Second method");
    }

    // Fix conflict
    @Override
    public void log(String string) {
        FirstInterface.super.log(string);
    }

    public static void main(String[] args) {
        MyClass myClass = new MyClass();
        
        myClass.firstMethod("Hello from firstMethod");
        myClass.secondMethod();
        

        myClass.log("Logging message");
    }
}
```

MyClass implements both FirstInterface and SecondInterface. Since both interfaces have a log method, we must resolve the conflict by overriding the log method in MyClass. Inside the overridden log method, we use `FirstInterface.super.log(string)` to explicitly call the log method from FirstInterface. This way, we decide which default method to use when there is a name conflict. We can also choose to call `SecondInterface.super.log(string)` if we want to use the default method from SecondInterface.

By overriding the conflicting default method in the implementing class and explicitly specifying which interface's method to call using the `InterfaceName.super.methodName` syntax, we can resolve the ambiguity and ensure the correct default method is used. This approach allows us to effectively manage multiple inheritance of interfaces with default methods in Java.