package bolbolestan.bolbolestanExceptions;

public class CapacityMismatchException extends Exception {
    public CapacityMismatchException(String classCode) {
        super("Number of registered exceeded capacity in course " + classCode + ".");
    }
}
