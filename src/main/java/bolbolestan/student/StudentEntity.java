package bolbolestan.student;

public class StudentEntity {
    private String id;
    private String name;
    private String secondName;
    private String birthDate;

    public StudentEntity() {}

    public StudentEntity(String studentId, String name, String secondName, String birthDate) {
        this.id = studentId;
        this.name = name;
        this.secondName = secondName;
        this.birthDate = birthDate;
    }

    public void setStudentId(String studentId) {
        this.id = studentId;
    }

    public String getStudentId() {
        return id;
    }

    public void setId(String studentId) {
        this.id = studentId;
    }

    public String getId() {
        return id;
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

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthDate() {
        return birthDate;
    }
}
