package IE.Bolbolestan.bolbolestanExceptions;

public class ClassCollisionException extends Exception {
    public ClassCollisionException(String classCode1, String classCode2) {
        super(" کلاس شماره " + classCode1 + " و کلاس شماره " + classCode2 + " با هم تداخل دارند. ");
        //"Class time code " + classCode1 + " and class time code " + classCode2 + " collide."
    }
}
