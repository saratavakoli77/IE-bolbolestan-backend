package IE.Bolbolestan.bolbolestanExceptions;

public class ExamTimeCollisionException extends Exception {
    public ExamTimeCollisionException(String classCode1, String classCode2) {
        super(" امتحان کلاس شماره " + classCode1 + " و امتحان کلاس شماره " + classCode2 + " با هم تداخل دارند. ");
        //super("Exam time of class code " + classCode1 + " and exam time of class code " + classCode2 + " collide.");
    }
}