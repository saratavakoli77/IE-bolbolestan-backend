package bolbolestan.student;

import bolbolestan.bolbolestanExceptions.StudentNotFoundException;

public class StudentModel {
    public StudentEntity getStudent(String studentId) throws StudentNotFoundException {
        return StudentStorage.getById(studentId);
    }

    public void addNewStudent(StudentEntity studentEntity) {
        //todo: validate data
        StudentStorage.add(studentEntity);
    }
}
