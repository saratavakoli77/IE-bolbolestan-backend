package IE.Bolbolestan.bolbolestanExceptions;

public class WeeklyScheduleDoesNotExistException extends Exception {
    public WeeklyScheduleDoesNotExistException() {
        super("برنامه هفتگی وجود ندارد");
    }
}
