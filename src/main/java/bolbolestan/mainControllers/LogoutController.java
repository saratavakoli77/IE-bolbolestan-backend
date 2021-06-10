package bolbolestan.mainControllers;

import bolbolestan.bolbolestanExceptions.StudentNotFoundException;
import bolbolestan.middlewares.Authentication;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/logout/")
public class LogoutController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Authentication.logoutStudent();
        response.sendRedirect(request.getContextPath() + "/login/");
    }
}