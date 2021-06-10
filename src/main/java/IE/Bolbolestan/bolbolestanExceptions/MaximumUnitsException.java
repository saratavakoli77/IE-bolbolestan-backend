package IE.Bolbolestan.bolbolestanExceptions;

public class MaximumUnitsException extends Exception {
    public MaximumUnitsException() {
        super("مجموع تعداد واحدها نمیتواند بیشتر از 20 باشد.");
    }
}
