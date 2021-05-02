package IE.Bolbolestan.weeklySchedule;

public class WeeklyScheduleOfferingEntity {
    private Integer id;
    private String studentId;
    private String offeringCode;


    public WeeklyScheduleOfferingEntity(Integer id, String offeringCode, String studentId) {
        this.id = id;
        this.offeringCode = offeringCode;
        this.studentId = studentId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getOfferingCode() {
        return offeringCode;
    }

    public void setOfferingCode(String offeringCode) {
        this.offeringCode = offeringCode;
    }
}
