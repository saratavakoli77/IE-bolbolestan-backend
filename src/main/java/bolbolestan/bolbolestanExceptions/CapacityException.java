package bolbolestan.bolbolestanExceptions;

public class CapacityException extends Exception {
    public CapacityException(String classCode) {
        super("Class code " + classCode + " capacity full.");
    }
}
