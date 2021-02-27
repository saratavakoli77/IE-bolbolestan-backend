package bolbolestan.weeklySchedule;

import bolbolestan.bolbolestanExceptions.OfferingCodeNotInWeeklyScheduleException;
import bolbolestan.bolbolestanExceptions.WeeklyScheduleDoesNotExistException;

import java.util.HashSet;
import java.util.Set;

public class WeeklyScheduleEntity {
    public static String FINALIZED_STATUS = "finalized";
    public static String NON_FINALIZED_STATUS = "non-finalized";

    private String studentId;
    private String status;
    private final Set<String> offeringCodes = new HashSet<>();

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
        this.offeringCodes.add(offeringCode);
    }

    public void removeFromOfferingCodes(String offeringCode) throws OfferingCodeNotInWeeklyScheduleException {
        if (this.offeringCodes.contains(offeringCode)) {
            this.offeringCodes.remove(offeringCode);
        } else {
            throw new OfferingCodeNotInWeeklyScheduleException();
        }

    }

    public Set<String> getOfferingCodes() {
        return offeringCodes;
    }
}
