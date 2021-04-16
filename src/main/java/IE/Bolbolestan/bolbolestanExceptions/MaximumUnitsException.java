package IE.Bolbolestan.bolbolestanExceptions;

public class MaximumUnitsException extends Exception {
    public MaximumUnitsException() {
        super("Number of units can not be more than 20.");
    }
}
