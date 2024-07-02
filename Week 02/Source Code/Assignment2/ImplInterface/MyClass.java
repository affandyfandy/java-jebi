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