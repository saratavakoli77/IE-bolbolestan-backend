package IE.Bolbolestan.mainControllers;

import IE.Bolbolestan.bolbolestanExceptions.StudentExistException;
import IE.Bolbolestan.student.StudentModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;


@RestController
@RequestMapping("/signup")
public class SignupController {
    @PostMapping("")
    public HashMap<String, Object> postLogin(
            @RequestBody HashMap<String, Object> request,
            final HttpServletResponse response
    ) throws IOException {
        try {
            new StudentModel().signupStudent(request);
            response.setStatus(HttpStatus.OK.value());
            return null;
        } catch (StudentExistException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            HashMap<String, Object> data = new HashMap<>();
            data.put("message", e.getMessage());
            return data;
        } catch (SQLException e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            HashMap<String, Object> data = new HashMap<>();
            data.put("message", "something went wrong!");
            return data;
        }
    }
}
