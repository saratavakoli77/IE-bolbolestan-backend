package IE.Bolbolestan.weeklySchedule;

import IE.Bolbolestan.bolbolestanExceptions.*;
import IE.Bolbolestan.middlewares.Authentication;
import IE.Bolbolestan.student.StudentEntity;
import IE.Bolbolestan.student.StudentModel;
import IE.Bolbolestan.tools.GetExceptionMessages;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/weekly_schedule")
public class WeeklyScheduleController extends HttpServlet {
    WeeklyScheduleModel model = new WeeklyScheduleModel();

    @GetMapping("")
    public HashMap<String, Object> getWeeklySchedule(
            final HttpServletResponse response, @RequestAttribute String studentId
    ) throws IOException {
        StudentEntity authenticatedStudent = Authentication.getAuthenticated(studentId);
        if (authenticatedStudent != null) {
            HashMap<String, Object> data = new HashMap<>();
            this.setStudentData(data, authenticatedStudent);
            this.setWeeklyScheduleOfferings(data, authenticatedStudent);
            return data;
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
        }
        return null;
    }

    @PostMapping("/submit")
    public HashMap<String, Object> postWeeklySchedule(
            final HttpServletResponse response, @RequestAttribute String studentId) throws IOException {
        StudentEntity authenticatedStudent = Authentication.getAuthenticated(studentId);
        if (authenticatedStudent != null) {
            HashMap<String, Object> data = new HashMap<>();
            Boolean noError = submitWeeklySchedule(data, authenticatedStudent);
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
            final HttpServletResponse response, @RequestAttribute String studentId
    ) throws IOException {
        StudentEntity authenticatedStudent = Authentication.getAuthenticated(studentId);
        if (authenticatedStudent != null) {
            resetWeeklySchedule(authenticatedStudent);
            response.setStatus(HttpStatus.OK.value());
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
        }
        return null;
    }

    private void resetWeeklySchedule(StudentEntity authenticatedStudent) {
        try {
            WeeklyScheduleEntity weeklyScheduleEntity = model.getWeeklySchedule(authenticatedStudent.getStudentId());
            model.resetWeeklySchedule(weeklyScheduleEntity);
        } catch (
                OfferingRecordNotFoundException |
                StudentNotFoundException |
                OfferingCodeNotInWeeklyScheduleException |
                SQLException e
        ) {
            e.printStackTrace();
        }

    }

    private Boolean submitWeeklySchedule(HashMap<String, Object> request, StudentEntity authenticatedStudent) {
        try {
            List<Exception> exceptionList = model.finalizeWeeklySchedule(authenticatedStudent.getStudentId());
            request.put("errorList", GetExceptionMessages.getExceptionMessages(exceptionList));
            return exceptionList.isEmpty();
        } catch (
                StudentNotFoundException |
                OfferingRecordNotFoundException |
                OfferingCodeNotInWeeklyScheduleException |
                OfferingNotFoundException |
                SQLException e
        ) {
            e.printStackTrace();
        }
        return false;
    }

    private void setWeeklyScheduleOfferings(
            HashMap<String, Object> request, StudentEntity authenticatedStudent) {
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

    private void setStudentData(HashMap<String, Object> request, StudentEntity authenticatedStudent) {
        request.put("studentId", authenticatedStudent.getStudentId());

        try {
            request.put("units", model.calculateUnitsSum(authenticatedStudent.getStudentId()));
            request.put("term", new StudentModel().getStudentCurrentTerm(authenticatedStudent.getStudentId()));
        } catch (StudentNotFoundException | OfferingNotFoundException | OfferingRecordNotFoundException e) {
            e.printStackTrace();
        }
    }

}