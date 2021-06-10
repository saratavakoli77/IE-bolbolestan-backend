package bolbolestan.bolbolestanExceptions;

public class PrerequisiteException extends Exception {
    public PrerequisiteException(String offeringCode) {
        super("Prerequisites of " + offeringCode + " have not been met.");
    }
}
