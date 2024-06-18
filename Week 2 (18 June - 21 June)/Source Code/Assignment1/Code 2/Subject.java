public class Subject {
    // States
    private String name;
    private String classId;

    // Constructor for creating Subject with name
    public Subject(String name) {
        this.name = name;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getClassId() {
        return classId;
    }

    // Setter for classId
    public void setClassId(String classId) {
        this.classId = classId;
    }
}
