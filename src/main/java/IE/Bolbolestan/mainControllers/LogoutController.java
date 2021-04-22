package IE.Bolbolestan.mainControllers;


import IE.Bolbolestan.middlewares.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/logout")
public class LogoutController {
    @PostMapping("")
    public void postLogout(final HttpServletResponse response) throws IOException {
        Authentication.logoutStudent();
        response.setStatus(HttpStatus.OK.value());
    }
}