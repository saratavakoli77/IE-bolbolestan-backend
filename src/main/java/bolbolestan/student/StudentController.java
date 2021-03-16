package bolbolestan.student;

import bolbolestan.bolbolestanExceptions.OfferingNotFoundException;
import bolbolestan.bolbolestanExceptions.StudentNotFoundException;
import bolbolestan.middlewares.Authentication;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet("/profile/")
public class StudentController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        StudentEntity authenticatedStudent = Authentication.getAuthenticated();

        if (authenticatedStudent != null) {
            try {
                this.getStudentProfile(authenticatedStudent.getStudentId(), request);
            } catch (StudentNotFoundException | OfferingNotFoundException e) {
                e.printStackTrace();
            }
            request.getRequestDispatcher("/WEB-INF/profile.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/login/");
        }
    }

    private void getStudentProfile(String studentId, HttpServletRequest request)
            throws StudentNotFoundException, OfferingNotFoundException {
        StudentModel model = new StudentModel();
        HashMap<String, Object> data = new HashMap<>();
        Map<String, Double> gpaTpu = model.getGpaTpu(studentId);
        data.put("student", model.getStudent(studentId));
        data.put("gpa", String.format("%.2f", gpaTpu.get("gpa")));
        data.put("tpu", String.format("%.0f", gpaTpu.get("tpu")));
        data.put("courses", model.getStudentPassedCourses(studentId));
        request.setAttribute("studentProfile", data);
    }

}
