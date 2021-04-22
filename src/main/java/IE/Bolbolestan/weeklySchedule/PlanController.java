package IE.Bolbolestan.weeklySchedule;

import IE.Bolbolestan.bolbolestanExceptions.OfferingNotFoundException;
import IE.Bolbolestan.bolbolestanExceptions.OfferingRecordNotFoundException;
import IE.Bolbolestan.bolbolestanExceptions.StudentNotFoundException;
import IE.Bolbolestan.middlewares.Authentication;
import IE.Bolbolestan.student.StudentEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/plan")
public class PlanController {
    @GetMapping("")
    public HashMap<String, Object> getPlan(final HttpServletResponse response) throws IOException {
        StudentEntity authenticatedStudent = Authentication.getAuthenticated();
        if (authenticatedStudent != null) {
            try {
                HashMap<String, Object> data = new HashMap<>();
                ArrayList<Object> plan;
                plan = new WeeklyScheduleModel().getWeeklySchedulePlan(Authentication.getAuthenticated().getStudentId());
                data.put("data", plan);
                response.setStatus(HttpStatus.OK.value());
                return data;
            } catch (StudentNotFoundException | OfferingNotFoundException | OfferingRecordNotFoundException e) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
                e.printStackTrace();
            }
        }
        response.sendError(HttpStatus.UNAUTHORIZED.value());
        return null;
    }
}
