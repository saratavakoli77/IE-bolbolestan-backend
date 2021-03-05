package bolbolestan.offeringRecord;

public class OfferingRecordEntity {
    private String offeringCode;
    private String studentId;
    private String status;
    private Integer grade;

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
