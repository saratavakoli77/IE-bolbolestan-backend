package IE.Bolbolestan.student;


import IE.Bolbolestan.bolbolestanExceptions.OfferingNotFoundException;
import IE.Bolbolestan.bolbolestanExceptions.StudentNotFoundException;
import IE.Bolbolestan.middlewares.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("")
public class StudentController {
    @GetMapping("")
    public HashMap<String, Object> getProfile(final HttpServletResponse response) throws IOException {
        StudentEntity authenticatedStudent = Authentication.getAuthenticated();
        if (authenticatedStudent != null) {
            try {
                return this.getStudentProfile(authenticatedStudent.getStudentId());
            } catch (StudentNotFoundException | OfferingNotFoundException e) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
        response.sendError(HttpStatus.UNAUTHORIZED.value());
        return null;
    }

    private HashMap<String, Object> getStudentProfile(String studentId)
            throws StudentNotFoundException, OfferingNotFoundException {
        StudentModel model = new StudentModel();
        HashMap<String, Object> data = new HashMap<>();
        Map<String, Double> gpaTpu = model.getGpaTpu(studentId);
        data.put("student", model.getStudent(studentId));
        data.put("gpa", String.format("%.2f", gpaTpu.get("gpa")));
        data.put("tpu", String.format("%.0f", gpaTpu.get("tpu")));
        data.put("courses", model.getStudentPassedCourses(studentId));
        return data;
    }
}
