package bolbolestan.student;

import bolbolestan.bolbolestanExceptions.OfferingNotFoundException;
import bolbolestan.bolbolestanExceptions.StudentNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class StudentView {
    private StudentModel model;

    public StudentView() {
        model = new StudentModel();
    }

    public HashMap<String, Object> getStudentProfile(String studentId)
            throws StudentNotFoundException, OfferingNotFoundException {
        HashMap<String, Object> data = new HashMap<>();
        Map<String, Double> gpaTpu = model.getGpaTpu(studentId);
        data.put("student", model.getStudent(studentId));
        data.put("gpa", gpaTpu.get("gpa"));
        data.put("tpu", gpaTpu.get("tpu"));
        data.put("courses", model.getStudentPassedCourses(studentId));
        return data;
    }
}
