package IE.Bolbolestan.student;


import IE.Bolbolestan.bolbolestanExceptions.StudentNotFoundException;
import IE.Bolbolestan.tools.HttpClient;
import IE.Bolbolestan.tools.refiners.StudentRefiner;

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

    public static List<StudentEntity> getAllStudents() {
        return studentEntities;
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

    public static void getDataFromApi() {
        HttpClient http = new HttpClient();
        String fetchProjectsUrl = "students";

        try {
            String response = http.get(fetchProjectsUrl);
            List<StudentEntity> students = new StudentRefiner(response).getRefinedEntities();
            studentEntities.addAll(students);
            System.out.println("Fetched " + students.size() + " students");
        } catch (Exception e) {
            System.out.println("error");
            e.fillInStackTrace();
        }
    }
}
