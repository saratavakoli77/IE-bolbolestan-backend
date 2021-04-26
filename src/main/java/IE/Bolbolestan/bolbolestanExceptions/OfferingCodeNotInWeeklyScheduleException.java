package IE.Bolbolestan.bolbolestanExceptions;

public class OfferingCodeNotInWeeklyScheduleException extends Exception {
    public OfferingCodeNotInWeeklyScheduleException() {
        super("کد درس در برنامه هفتگی وجود ندارد.");
    }
}
