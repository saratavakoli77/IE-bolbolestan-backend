package IE.Bolbolestan.bolbolestanExceptions;

public class TokenExpiredException extends Exception {
    public TokenExpiredException() {
        super("توکن شما منقضی شده است.");
    }
}
