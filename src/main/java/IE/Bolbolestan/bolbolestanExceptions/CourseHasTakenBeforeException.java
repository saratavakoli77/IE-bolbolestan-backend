package IE.Bolbolestan.bolbolestanExceptions;

public class CourseHasTakenBeforeException extends Exception {
    public CourseHasTakenBeforeException(String offeringCode) {
        super("درس با کد " + offeringCode + " قبلا پاس شده است.");
    }
}
