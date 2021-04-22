package IE.Bolbolestan.mainControllers;

import IE.Bolbolestan.bolbolestanExceptions.StudentNotFoundException;
import IE.Bolbolestan.middlewares.Authentication;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;


@RestController
@RequestMapping("/login")
public class LoginController {
    @PostMapping("")
    public void postLogin(@RequestBody HashMap<String, Object> request, final HttpServletResponse response) throws IOException {
        try {
            String studentId = (String) request.get("studentId");
            Authentication.authenticateStudent(studentId);
            response.setStatus(HttpStatus.OK.value());
        } catch (StudentNotFoundException e) {
            response.sendError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }
}
