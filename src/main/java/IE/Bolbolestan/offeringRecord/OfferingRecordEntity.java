package IE.Bolbolestan.offeringRecord;

public class OfferingRecordEntity {
    public static String FINALIZED_STATUS = "finalized";
    public static String NON_FINALIZED_STATUS = "non-finalized";
    public static String NON_FINALIZED_WAIT = "non-finalized-wait";
    public static String FINALIZED_WAIT = "finalized-wait";
    public static String REMOVED_WAIT = "removed-wait";
    public static String COMPLETED_STATUS = "completed";
    public static String REMOVED_STATUS = "removed";

    public static int PASSED_GRADE = 10;

    private Integer id;
    private String offeringCode;
    private String studentId;
    private String status;
    private Double grade;
    private String code;
    private Integer term;

    public OfferingRecordEntity() {}

    public OfferingRecordEntity(
            Integer id,
            String studentId,
            String offeringCode,
            Double grade,
            String status,
            Integer term
    ) {
        this.id = id;
        this.studentId = studentId;
        this.offeringCode = offeringCode;
        this.grade = grade;
        this.status = status;
        this.code = offeringCode.substring(0, offeringCode.length() - 2);
        this.term = term;
    }

    public OfferingRecordEntity(String studentId, String offeringCode, Double grade, String status) {
        this.studentId = studentId;
        this.offeringCode = offeringCode;
        this.grade = grade;
        this.status = status;
        this.code = offeringCode.substring(0, offeringCode.length() - 2);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
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

    public void setTerm(Integer term) {
        this.term = term;
    }

    public Integer getTerm() {
        return term;
    }
}
