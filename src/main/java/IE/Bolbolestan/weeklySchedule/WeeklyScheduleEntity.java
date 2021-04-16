package IE.Bolbolestan.weeklySchedule;

import IE.Bolbolestan.bolbolestanExceptions.OfferingCodeNotInWeeklyScheduleException;

import java.util.ArrayList;
import java.util.List;


public class WeeklyScheduleEntity {
    public static String FINALIZED_STATUS = "finalized";
    public static String NON_FINALIZED_STATUS = "non-finalized";

    private String studentId;
    private String status;
    private final List<String> offeringCodes = new ArrayList<>();

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

    public void addToOfferingCodes(String offeringCode) {
        if (!this.offeringCodes.contains(offeringCode)) {
            this.offeringCodes.add(offeringCode);
        }
    }

    public void removeFromOfferingCodes(String offeringCode) throws OfferingCodeNotInWeeklyScheduleException {
        if (this.offeringCodes.contains(offeringCode)) {
            this.offeringCodes.remove(offeringCode);
        } else {
            throw new OfferingCodeNotInWeeklyScheduleException();
        }

    }

    public List<String> getOfferingCodes() {
        return offeringCodes;
    }
}
