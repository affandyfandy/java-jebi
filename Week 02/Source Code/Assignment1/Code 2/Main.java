public class Main {
    public static void main(String[] args) {
        // Creating a Teacher with name and age
        Teacher tamTeacher = new Teacher("Tam", 35);

        // Creating a Subject with name
        Subject mathSubject = new Subject("Mathematics");
        mathSubject.setClassId("1");

        // Teaching the subject
        tamTeacher.teach(mathSubject);
    }
}
