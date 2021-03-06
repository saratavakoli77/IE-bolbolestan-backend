package IE.Bolbolestan.student;

public class StudentEntity {
    private String id;
    private String name;
    private String secondName;
    private String birthDate;
    private String field;
    private String faculty;
    private String level;
    private String status;
    private String img;
    private String email;
    private String password;

    public StudentEntity() {}

    public StudentEntity(
            String studentId,
            String name,
            String secondName,
            String birthDate,
            String field,
            String faculty,
            String level,
            String status,
            String img,
            String email,
            String password) {
        this.id = studentId;
        this.name = name;
        this.secondName = secondName;
        this.birthDate = birthDate;
        this.field = field;
        this.faculty = faculty;
        this.level = level;
        this.status = status;
        this.img = img;
        this.email = email;
        this.password = password;
    }

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

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
