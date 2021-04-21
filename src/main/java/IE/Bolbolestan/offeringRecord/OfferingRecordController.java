package IE.Bolbolestan.offeringRecord;

import IE.Bolbolestan.bolbolestanExceptions.*;
import IE.Bolbolestan.middlewares.Authentication;
import IE.Bolbolestan.middlewares.SearchHistory;
import IE.Bolbolestan.offering.OfferingEntity;
import IE.Bolbolestan.offering.OfferingModel;
import IE.Bolbolestan.student.StudentEntity;
import IE.Bolbolestan.tools.GetExceptionMessages;
import IE.Bolbolestan.weeklySchedule.WeeklyScheduleModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class OfferingRecordController extends HttpServlet {
    WeeklyScheduleModel model = new WeeklyScheduleModel();
    StudentEntity authenticatedStudent;

    @GetMapping("")
    public HashMap<String, Object> getCourses(
            @RequestParam(required = false) String search,
            final HttpServletResponse response
    ) throws IOException {
        this.authenticatedStudent = Authentication.getAuthenticated();
        if (this.authenticatedStudent != null) {
            HashMap<String, Object> data = new HashMap<>();
            if (search != null) {
                this.getMatchedOfferings(data, search);
            } else {
                this.clearSearchHistory(data);
            }
            this.setOfferingEntities(data);
            response.setStatus(HttpStatus.OK.value());
            return data;
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
        }
        return null;
    }

    @PostMapping("/{courseCode}/{classCode}")
    public HashMap<String, Object> postCourses(
            @PathVariable String courseCode,
            @PathVariable String classCode,
            final HttpServletResponse response
    ) throws IOException {
        this.authenticatedStudent = Authentication.getAuthenticated();
        if (this.authenticatedStudent != null) {

            HashMap<String, Object> data = new HashMap<>();
            Boolean noError = addToWeeklySchedule(data, courseCode, classCode);
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

    @DeleteMapping("/{courseCode}/{classCode}")
    public HashMap<String, Object> deleteCourses(
            @PathVariable String courseCode,
            @PathVariable String classCode,
            final HttpServletResponse response
    ) throws IOException {
        this.authenticatedStudent = Authentication.getAuthenticated();
        if (this.authenticatedStudent != null) {
            removeFromWeeklySchedule(courseCode, classCode);
            response.setStatus(HttpStatus.OK.value());
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
        }
        return null;
    }

    private void getMatchedOfferings(HashMap<String, Object> request, String searchValue) {
        SearchHistory.setLastSearch(searchValue);
        setOfferingEntities(request);
    }

    private void clearSearchHistory(HashMap<String, Object> request) {
        SearchHistory.setLastSearch("");
        setOfferingEntities(request);
    }

    private void setOfferingEntities(HashMap<String, Object> request) {
        List<OfferingEntity> offeringEntities;
        OfferingModel offeringModel = new OfferingModel();
        if (SearchHistory.getLastSearch().equals("")) {
            offeringEntities = offeringModel.getOfferings();
        } else {
            offeringEntities = offeringModel.getSearchResult(SearchHistory.getLastSearch());
        }
        request.put("offeringEntities", offeringEntities);
    }

    private Boolean addToWeeklySchedule(HashMap<String, Object> request, String courseCode, String classCode) {
        List<Exception> exceptions;
        try {
            exceptions = model.addToWeeklySchedule(
                    authenticatedStudent.getStudentId(), courseCode + classCode
            );
            request.put("errorList", GetExceptionMessages.getExceptionMessages(exceptions));
            return exceptions.isEmpty();
        } catch (StudentNotFoundException | OfferingNotFoundException | OfferingRecordNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void removeFromWeeklySchedule(String courseCode, String classCode) {
        try {
            model.removeFromWeeklySchedule(authenticatedStudent.getStudentId(), courseCode + classCode);
        } catch (
                StudentNotFoundException |
                        OfferingCodeNotInWeeklyScheduleException |
                OfferingRecordNotFoundException |
                        WeeklyScheduleDoesNotExistException e
        ) {
            e.printStackTrace();
        }
    }
}