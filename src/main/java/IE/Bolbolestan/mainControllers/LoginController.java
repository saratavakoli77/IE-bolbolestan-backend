package IE.Bolbolestan.mainControllers;

import IE.Bolbolestan.bolbolestanExceptions.EmailOrPasswordIncorrectException;
import IE.Bolbolestan.middlewares.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;


@RestController
@RequestMapping("/login")
public class LoginController {
    @PostMapping("")
    public HashMap<String, Object> postLogin(
            @RequestBody HashMap<String, Object> request,
            final HttpServletResponse response
    ) throws IOException {
        HashMap<String, Object> data = new HashMap<>();
        try {
            String email = (String) request.get("email");
            String password = (String) request.get("password");
            data.put("jwt", Authentication.authenticate(email, password));
            response.setStatus(HttpStatus.OK.value());
        } catch (EmailOrPasswordIncorrectException e) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            data.put("message", e.getMessage());
        } catch (SQLException e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            data.put("message", "something went wrong!");
        }
        return data;
    }
}
