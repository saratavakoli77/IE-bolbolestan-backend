package IE.Bolbolestan.mainControllers;

import IE.Bolbolestan.bolbolestanExceptions.StudentNotFoundException;
import IE.Bolbolestan.middlewares.Authentication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;


@RestController
@RequestMapping("/change-password")
public class ChangePasswordController {
    @PostMapping("")
    public HashMap<String, Object> postChangePassword(
            @RequestBody HashMap<String, Object> request,
            @RequestParam String token,
            final HttpServletResponse response
    ) throws IOException {
        try {
            String newPassword = (String) request.get("new_password");
            Claims claims = Authentication.decodeJWT(token);
            String studentId = claims.getId();
            Authentication.changePassword(studentId, newPassword);
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
