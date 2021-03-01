package bolbolestan.student;

import bolbolestan.bolbolestanExceptions.StudentNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class StudentStorage {
    private static final List<StudentEntity> studentEntities = new ArrayList<>();

    public static void add(StudentEntity studentEntity) {
        studentEntities.add(studentEntity);
    }

    public static void remove(StudentEntity studentEntity) {
        studentEntities.remove(studentEntity);
    }

    public static StudentEntity getById(String studentId) throws StudentNotFoundException {
        for (StudentEntity entity: studentEntities) {
            if (entity.getStudentId().equals(studentId)) {
                return entity;
            }
        }
        throw new StudentNotFoundException();
    }

    public static void removeAll() {
        studentEntities.clear();
    }
}
