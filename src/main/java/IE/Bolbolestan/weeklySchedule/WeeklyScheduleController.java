package IE.Bolbolestan.weeklySchedule;

import IE.Bolbolestan.bolbolestanExceptions.*;
import IE.Bolbolestan.middlewares.Authentication;
import IE.Bolbolestan.student.StudentEntity;
import IE.Bolbolestan.tools.GetExceptionMessages;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/weekly_schedule")
public class WeeklyScheduleController extends HttpServlet {
    WeeklyScheduleModel model = new WeeklyScheduleModel();
    StudentEntity authenticatedStudent;

    @GetMapping("")
    public HashMap<String, Object> getWeeklySchedule(
            final HttpServletResponse response
    ) throws IOException {
        this.authenticatedStudent = Authentication.getAuthenticated();
        if (this.authenticatedStudent != null) {
            HashMap<String, Object> data = new HashMap<>();
            this.setStudentData(data);
            this.setWeeklyScheduleOfferings(data);
            return data;
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
        }
        return null;
    }

    @PostMapping("/submit")
    public HashMap<String, Object> postWeeklySchedule(final HttpServletResponse response) throws IOException {
        this.authenticatedStudent = Authentication.getAuthenticated();
        if (this.authenticatedStudent != null) {
            HashMap<String, Object> data = new HashMap<>();
            Boolean noError = submitWeeklySchedule(data);
            if (noError) {
                response.setStatus(HttpStatus.OK.value());
            } else {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                return data;
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
        }
        return null;
    }


    @DeleteMapping("")
    public HashMap<String, Object> deleteWeeklySchedule(
            final HttpServletResponse response
    ) throws IOException {
        this.authenticatedStudent = Authentication.getAuthenticated();
        if (this.authenticatedStudent != null) {
            resetWeeklySchedule();
            response.setStatus(HttpStatus.OK.value());
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
        }
        return null;
    }

    private void resetWeeklySchedule() {
        try {
            WeeklyScheduleEntity weeklyScheduleEntity = model.getWeeklySchedule(authenticatedStudent.getStudentId());
            model.resetWeeklySchedule(weeklyScheduleEntity);
        } catch (OfferingRecordNotFoundException | StudentNotFoundException | OfferingCodeNotInWeeklyScheduleException e) {
            e.printStackTrace();
        }

    }

    private Boolean submitWeeklySchedule(HashMap<String, Object> request) {
        try {
            List<Exception> exceptionList = model.finalizeWeeklySchedule(authenticatedStudent.getStudentId());
            request.put("errorList", GetExceptionMessages.getExceptionMessages(exceptionList));
            return exceptionList.isEmpty();
        } catch (StudentNotFoundException | OfferingRecordNotFoundException | OfferingCodeNotInWeeklyScheduleException | OfferingNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setWeeklyScheduleOfferings(HashMap<String, Object> request) {
        try {
            WeeklyScheduleEntity weeklyScheduleEntity = model.getWeeklySchedule(authenticatedStudent.getStudentId());

            request.put(
                    "weeklyScheduleOfferings",
                    model.getWeeklyScheduleOfferingEntities(weeklyScheduleEntity)
            );
        } catch (StudentNotFoundException | OfferingNotFoundException | OfferingRecordNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setStudentData(HashMap<String, Object> request) {
        request.put("studentId", authenticatedStudent.getStudentId());

        try {
            request.put("units", model.calculateUnitsSum(authenticatedStudent.getStudentId()));
        } catch (StudentNotFoundException | OfferingNotFoundException | OfferingRecordNotFoundException e) {
            e.printStackTrace();
        }
    }

}