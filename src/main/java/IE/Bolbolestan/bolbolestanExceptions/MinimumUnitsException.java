package IE.Bolbolestan.bolbolestanExceptions;

public class MinimumUnitsException extends Exception {
    public MinimumUnitsException() {
        super("مجموع تعداد واحدها نمیتواند کمتر از 12 باشد.");
    }
}
