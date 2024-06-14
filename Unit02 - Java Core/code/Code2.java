public class Code2 {
    public static void main(String[] args) {
        MyClass obj = new MyClass();
        obj.value = 5;
        obj.name = "Object2";
        changeReference(obj);
        System.out.println("obj.value after changeReference: " + obj.value);
        System.out.println("obj.name after changeReference: " + obj.name);
    }

    public static void changeReference(MyClass x) {
        x = new MyClass();
        x.value = 10;
        x.name = "NewObject2";
    }
}

class MyClass {
    int value;
    String name; // New field
}
