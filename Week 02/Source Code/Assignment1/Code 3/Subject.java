public class Subject {
    // States
    private String name;
    private String classId;

    // Constructor for creating Subject with name and classId
    public Subject(String name, String classId) {
        this.name = name;
        this.classId = classId;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getClassId() {
        return classId;
    }
}
