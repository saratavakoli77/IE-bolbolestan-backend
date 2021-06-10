package IE.Bolbolestan.bolbolestanExceptions;

public class PrerequisiteException extends Exception {
    public PrerequisiteException(String offeringCode) {
        super("پیش نیازی های درس با کد  " + offeringCode + " رعایت نشده است.");
    }
}
