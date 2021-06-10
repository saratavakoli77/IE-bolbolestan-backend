package bolbolestan.bolbolestanExceptions;

public class OfferingCodeNotInWeeklyScheduleException extends Exception {
    public OfferingCodeNotInWeeklyScheduleException() {
        super("Offering code not in weekly schedule.");
    }
}
