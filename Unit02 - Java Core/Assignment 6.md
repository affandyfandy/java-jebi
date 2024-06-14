### Assignment 6
Explain previous slice as slice 6 (stack & heap).

#
## **Code 1 : Passing Object Reference to a Method**
```java
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
    String name;
}
```


- **Heap Memory**: When main() starts, it allocates memory for obj of type MyClass on the heap. The object has fields value and name, which are initially set to 5 and "Object1" respectively.
- **Stack Memory**: main() method's stack frame contains a reference obj to the memory location of the MyClass object on the heap.
- **Passing by Reference**: When modifyObject(obj) is called, a new stack frame is created for modifyObject. The reference x now points to the same memory location in the heap as obj. Therefore, changes to x inside modifyObject directly affect the MyClass object that obj references.
- **Output**: After modifyObject completes, obj.value is 10 and obj.name is "ModifiedObject1", demonstrating that modifications via x affect the actual object obj references.


#
## **Code 2 : Changing Reference to a New Object**
```java
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
    String name; 
}
```
- **Heap Memory**: Similar to Code 1, main() allocates memory for obj of type MyClass on the heap, with fields value (5) and name ("Object2").
- **Stack Memory**: main()'s stack frame contains a reference obj pointing to the heap object.
- **Changing Reference**: In changeReference, x initially points to the same heap object as obj. However, within changeReference, x is reassigned to a new instance of MyClass. This new instance (x) has its own memory allocation on the heap with value set to 10 and name set to "NewObject2".
- **Effect of Reassignment**: The reassignment of x inside changeReference does not affect the original reference obj in main(). Therefore, after changeReference completes, obj.value is still 5 and obj.name remains "Object2".
- **Output**: This highlights that changes to x (reassignment to a new object) are local to changeReference and do not affect the original object reference obj outside of this method.



#
In Code1, modifications to the object via x affect the object that obj references, demonstrating that both x and obj point to the same heap object. In Code2, x is reassigned to a new object within the method, which does not impact the original object that obj references, highlighting that changes to local references do not affect the references outside the method.