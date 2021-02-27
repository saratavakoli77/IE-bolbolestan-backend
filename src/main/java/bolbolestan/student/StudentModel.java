package bolbolestan.student;

import bolbolestan.bolbolestanExceptions.StudentNotFoundException;

public class StudentModel {
    public StudentEntity getStudent(String studentId) throws StudentNotFoundException {
        return StudentStorage.getById(studentId);
    }
}
