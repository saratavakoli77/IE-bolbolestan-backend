package IE.Bolbolestan.bolbolestanExceptions;

public class EmailOrPasswordIncorrectException extends Exception {
    public EmailOrPasswordIncorrectException() {
        super("ایمیل یا رمز عبور وارد شده نادرست است.");
    }
}
