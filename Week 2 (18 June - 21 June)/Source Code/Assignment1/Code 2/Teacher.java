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
