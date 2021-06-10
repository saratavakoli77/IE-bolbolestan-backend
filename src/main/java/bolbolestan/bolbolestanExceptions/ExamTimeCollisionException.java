package bolbolestan.bolbolestanExceptions;

public class ExamTimeCollisionException extends Exception {
    public ExamTimeCollisionException(String classCode1, String classCode2) {
        super("Exam time of class code " + classCode1 + " and exam time of class code " + classCode2 + " collide.");
    }
}