package bolbolestan.bolbolestanExceptions;

public class WeeklyScheduleDoesNotExistException extends Exception {
    public WeeklyScheduleDoesNotExistException() {
        super("Weekly schedule does not exist.");
    }
}
