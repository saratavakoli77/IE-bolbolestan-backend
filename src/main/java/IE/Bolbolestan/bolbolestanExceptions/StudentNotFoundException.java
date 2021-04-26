package IE.Bolbolestan.bolbolestanExceptions;

public class StudentNotFoundException extends Exception {
    public StudentNotFoundException() {
        super("دانشجو با شماره دانشجویی داده شده یافت نشد.");
    }
}
