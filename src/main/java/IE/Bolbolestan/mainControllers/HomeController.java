package IE.Bolbolestan.mainControllers;


import IE.Bolbolestan.middlewares.Authentication;
import IE.Bolbolestan.student.StudentEntity;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("")
public class HomeController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        StudentEntity authenticatedStudent = Authentication.getAuthenticated();

        if (authenticatedStudent != null) {
            request.setAttribute("authenticated", true);
            request.setAttribute("studentId", authenticatedStudent.getStudentId());
            request.getRequestDispatcher("/WEB-INF/home.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/login/");

        }
    }

}
