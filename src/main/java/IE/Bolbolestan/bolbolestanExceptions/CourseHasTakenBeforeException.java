package IE.Bolbolestan.bolbolestanExceptions;

public class CourseHasTakenBeforeException extends Exception {
    public CourseHasTakenBeforeException(String offeringCode) {
        super("Course " + offeringCode + " has taken before.");
    }
}
