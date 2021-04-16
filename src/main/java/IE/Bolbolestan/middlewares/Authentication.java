package IE.Bolbolestan.middlewares;

import IE.Bolbolestan.bolbolestanExceptions.StudentNotFoundException;
import IE.Bolbolestan.student.StudentEntity;
import IE.Bolbolestan.student.StudentModel;


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
