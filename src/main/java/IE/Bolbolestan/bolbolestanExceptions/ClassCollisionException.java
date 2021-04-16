package IE.Bolbolestan.bolbolestanExceptions;

public class ClassCollisionException extends Exception {
    public ClassCollisionException(String classCode1, String classCode2) {
        super("Class time code " + classCode1 + " and class time code " + classCode2 + " collide.");
    }
}
