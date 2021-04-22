package IE.Bolbolestan.mainControllers;

import IE.Bolbolestan.bolbolestanExceptions.StudentNotFoundException;
import IE.Bolbolestan.middlewares.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@RequestMapping("/login")
public class LoginController {
    @PostMapping("")
    public void postLogin(@RequestParam String studentId, final HttpServletResponse response) throws IOException {
        try {
            Authentication.authenticateStudent(studentId);
            response.setStatus(HttpStatus.OK.value());
        } catch (StudentNotFoundException e) {
            response.sendError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }
}
