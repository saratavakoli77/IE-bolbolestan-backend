package IE.Bolbolestan.course;

public class PrerequisiteEntity {
    private Integer id;
    private String mainCourseCode;
    private String prerequisiteCourseCode;

    public PrerequisiteEntity(Integer id, String mainCourseCode, String prerequisiteCourseCode) {
        this.id = id;
        this.mainCourseCode = mainCourseCode;
        this.prerequisiteCourseCode = prerequisiteCourseCode;
    }

    public PrerequisiteEntity(String mainCourseCode, String prerequisiteCourseCode) {
        this.mainCourseCode = mainCourseCode;
        this.prerequisiteCourseCode = prerequisiteCourseCode;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getMainCourseCode() {
        return mainCourseCode;
    }

    public void setMainCourseCode(String mainCourseCode) {
        this.mainCourseCode = mainCourseCode;
    }

    public String getPrerequisiteCourseCode() {
        return prerequisiteCourseCode;
    }

    public void setPrerequisiteCourseCode(String prerequisiteCourseCode) {
        this.prerequisiteCourseCode = prerequisiteCourseCode;
    }
}
