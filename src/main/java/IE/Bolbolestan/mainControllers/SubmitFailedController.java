package IE.Bolbolestan.mainControllers;

import IE.Bolbolestan.middlewares.Authentication;
import IE.Bolbolestan.student.StudentEntity;
import org.springframework.web.bind.annotation.RequestAttribute;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/submit_failed/")
public class SubmitFailedController extends HttpServlet {
    protected void doPost(
            HttpServletRequest request, HttpServletResponse response, @RequestAttribute String studentId)
            throws ServletException, IOException {
        StudentEntity authenticatedStudent = Authentication.getAuthenticated(studentId);
        if (authenticatedStudent != null) {
            request.getRequestDispatcher("/WEB-INF/submit_failed.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/login/");
        }
    }
}
