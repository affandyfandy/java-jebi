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
