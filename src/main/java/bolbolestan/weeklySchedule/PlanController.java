package bolbolestan.weeklySchedule;

import bolbolestan.bolbolestanExceptions.OfferingNotFoundException;
import bolbolestan.bolbolestanExceptions.StudentNotFoundException;
import bolbolestan.middlewares.Authentication;
import bolbolestan.student.StudentEntity;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@WebServlet("/plan/")
public class PlanController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        StudentEntity authenticatedStudent = Authentication.getAuthenticated();

        if (authenticatedStudent != null) {
            request.setAttribute("studentId", authenticatedStudent.getStudentId());
            request.getRequestDispatcher("/WEB-INF/plan.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/login/");
        }
    }
}
