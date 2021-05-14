package IE.Bolbolestan.mainControllers;

import IE.Bolbolestan.bolbolestanExceptions.StudentNotFoundException;
import IE.Bolbolestan.middlewares.Authentication;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;


@RestController
@RequestMapping("/forgot-password")
public class ForgotPasswordController {
    @PostMapping("")
    public HashMap<String, Object> postChangePassword(
            @RequestBody HashMap<String, Object> request,
            final HttpServletResponse response
    ) throws IOException {
        try {
            String email = (String) request.get("email");
            Authentication.forgotPassword(email);
            response.setStatus(HttpStatus.OK.value());
            return null;
        } catch (JwtException | StudentNotFoundException e) {
            HashMap<String, Object> data = new HashMap<>();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return data;
        } catch (SQLException e) {
            HashMap<String, Object> data = new HashMap<>();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return data;
        }
    }
}
