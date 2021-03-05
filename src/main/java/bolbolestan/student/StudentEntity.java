package bolbolestan.student;

public class StudentEntity {
    private String studentId;
    private String name;
    private String secondName;
    private String enteredAt;

    public StudentEntity() {}

    public StudentEntity(String studentId, String name, String secondName, String enteredAt) {
        this.studentId = studentId;
        this.name = name;
        this.secondName = secondName;
        this.enteredAt = enteredAt;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setEnteredAt(String enteredAt) {
        this.enteredAt = enteredAt;
    }

    public String getEnteredAt() {
        return enteredAt;
    }
}
