package bolbolestan.bolbolestanExceptions;

public class MinimumUnitsException extends Exception {
    public MinimumUnitsException() {
        super("Number of units can not be under 12.");
    }
}
