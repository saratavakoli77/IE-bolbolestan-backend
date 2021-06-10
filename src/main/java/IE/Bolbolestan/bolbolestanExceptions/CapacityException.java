package IE.Bolbolestan.bolbolestanExceptions;

public class CapacityException extends Exception {
    public CapacityException(String classCode) {
        super(" ظرفیت درس با کد " + classCode + " تکمیل است. ");
    }
}
