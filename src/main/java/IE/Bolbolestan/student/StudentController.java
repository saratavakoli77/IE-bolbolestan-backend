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
@RequestMapping("/profile")
public class StudentController {
    @GetMapping("")
    public HashMap<String, Object> getProfile(final HttpServletResponse response) throws IOException {
        StudentEntity authenticatedStudent = Authentication.getAuthenticated();
        if (authenticatedStudent != null) {
            try {
                response.setStatus(HttpStatus.OK.value());
                return this.getStudentProfile(authenticatedStudent.getStudentId());
            } catch (StudentNotFoundException e) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                HashMap<String, Object> data = new HashMap<>();
                data.put("message", "دانشجو با شماره دانشجویی داده شده یافت نشد.");
                e.printStackTrace();
                return data;
            } catch (OfferingNotFoundException e) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                HashMap<String, Object> data = new HashMap<>();
                data.put("message", "درس با کد داده شده یافت نشد.");
                e.printStackTrace();
                return data;
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
        data.put("courses", model.getFormattedPassedCourses(studentId));
        return data;
    }
}
