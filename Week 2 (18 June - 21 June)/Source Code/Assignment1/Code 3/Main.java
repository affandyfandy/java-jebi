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
