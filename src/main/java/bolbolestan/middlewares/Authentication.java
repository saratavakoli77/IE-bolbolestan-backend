package bolbolestan.middlewares;

import bolbolestan.bolbolestanExceptions.StudentNotFoundException;
import bolbolestan.student.StudentEntity;
import bolbolestan.student.StudentModel;

public class Authentication {
    private static StudentEntity currentStudent;

    public static void authenticateStudent(String studentId) throws StudentNotFoundException {
        currentStudent = new StudentModel().getStudent(studentId);
    }

    public static StudentEntity getAuthenticated() {
        return currentStudent;
    }

    public static void logoutStudent() {
        currentStudent = null;
        SearchHistory.setLastSearch("");
    }
}
