package bolbolestan.offeringRecord;

public class OfferingRecordEntity {
    public static String FINALIZED_STATUS = "finalized";
    public static String NON_FINALIZED_STATUS = "non-finalized";
    public static String COMPLETED_STATUS = "completed";

    private String offeringCode;
    private String studentId;
    private String status;
    private Integer grade;

    public OfferingRecordEntity() {}

    public OfferingRecordEntity(String studentId, String offeringCode, Integer grade, String status) {
        this.studentId = studentId;
        this.offeringCode = offeringCode;
        this.grade = grade;
        this.status = status;
    }

    public void setOfferingCode(String offeringCode) {
        this.offeringCode = offeringCode;
    }

    public String getOfferingCode() {
        return offeringCode;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getGrade() {
        return grade;
    }
}
