package IE.Bolbolestan.bolbolestanExceptions;

public class StudentExistException extends Exception {
    public StudentExistException() {
        super("ایمیل یا شماره دانشجویی وارد شده تکراری است.");
    }
}
