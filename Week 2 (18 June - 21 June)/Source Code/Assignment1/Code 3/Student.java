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
