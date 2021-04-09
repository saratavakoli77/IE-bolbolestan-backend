package bolbolestan.offeringRecord;

public class OfferingRecordEntity {
    public static String FINALIZED_STATUS = "finalized";
    public static String NON_FINALIZED_STATUS = "non-finalized";
    public static String COMPLETED_STATUS = "completed";
    public static String REMOVED_STATUS = "removed";

    public static int PASSED_GRADE = 10;

    private String offeringCode;
    private String studentId;
    private String status;
    private Double grade;
    private String code;
    private String term;

    public OfferingRecordEntity() {}

    public OfferingRecordEntity(String studentId, String offeringCode, Double grade, String status) {
        this.studentId = studentId;
        this.offeringCode = offeringCode;
        this.grade = grade;
        this.status = status;
        this.code = offeringCode.substring(0, offeringCode.length() - 2);
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

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public Double getGrade() {
        return grade;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getTerm() {
        return term;
    }
}
