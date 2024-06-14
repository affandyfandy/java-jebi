public class Code1 {
    public static void main(String[] args) {
        MyClass obj = new MyClass();
        obj.value = 5;
        obj.name = "Object1";
        modifyObject(obj);
        System.out.println("obj.value after modifyObject: " + obj.value);
        System.out.println("obj.name after modifyObject: " + obj.name);
    }

    public static void modifyObject(MyClass x) {
        x.value = 10;
        x.name = "ModifiedObject1";
    }
}

class MyClass {
    int value;
    String name; // New field
}
