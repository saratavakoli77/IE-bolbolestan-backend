package IE.Bolbolestan.weeklySchedule;

import IE.Bolbolestan.bolbolestanExceptions.*;
import IE.Bolbolestan.middlewares.Authentication;
import IE.Bolbolestan.middlewares.SearchHistory;
import IE.Bolbolestan.offering.OfferingEntity;
import IE.Bolbolestan.offering.OfferingModel;
import IE.Bolbolestan.student.StudentEntity;
import IE.Bolbolestan.tools.GetExceptionMessages;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/courses/")
public class WeeklyScheduleController extends HttpServlet {
    WeeklyScheduleModel model = new WeeklyScheduleModel();
    StudentEntity authenticatedStudent;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        authenticatedStudent = Authentication.getAuthenticated();
        request.setAttribute("searchValue", SearchHistory.getLastSearch());

        if (authenticatedStudent != null) {
            this.setStudentData(request);
            this.setWeeklyScheduleOfferings(request);
            this.setOfferingEntities(request);
            request.getRequestDispatcher("/WEB-INF/courses.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/login/");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        switch (request.getParameter("action")) {
            case "submit":
                Boolean noError = submitWeeklySchedule(request);
                if (noError) {
                    response.sendRedirect(request.getContextPath() + "/plan/");
                } else {
                    RequestDispatcher rd = request.getRequestDispatcher("/submit_failed/");
                    rd.forward(request, response);
                }
                break;
            case "reset":
                resetWeeklySchedule();
                response.sendRedirect(request.getContextPath() + "/courses/");
                break;
            case "remove":
                removeFromWeeklySchedule(request);
                response.sendRedirect(request.getContextPath() + "/courses/");
                break;
            case "add":
                addToWeeklySchedule(request);
                response.sendRedirect(request.getContextPath() + "/courses/");
                break;
            case "search":
                getMatchedOfferings(request);
                response.sendRedirect(request.getContextPath() + "/courses/");
                break;
            case "clear":
                clearSearchHistory(request);
                response.sendRedirect(request.getContextPath() + "/courses/");
                break;
            default:
        }

    }

    private void getMatchedOfferings(HttpServletRequest request) {
        String searchValue = request.getParameter("search");
        SearchHistory.setLastSearch(searchValue);
        setOfferingEntities(request);
    }

    private void clearSearchHistory(HttpServletRequest request) {
        SearchHistory.setLastSearch("");
        setOfferingEntities(request);
    }

    private void setOfferingEntities(HttpServletRequest request) {
        List<OfferingEntity> offeringEntities;
        OfferingModel offeringModel = new OfferingModel();
        if (SearchHistory.getLastSearch().equals("")) {
            offeringEntities = offeringModel.getOfferings();
        } else {
            offeringEntities = offeringModel.getSearchResult(SearchHistory.getLastSearch());
        }
        request.setAttribute("offeringEntities", offeringEntities);
    }

    private void addToWeeklySchedule(HttpServletRequest request) {
        String classCode = request.getParameter("class_code");
        String courseCode = request.getParameter("course_code");
        List<Exception> exceptions;
        try {
            exceptions = model.addToWeeklySchedule(
                    authenticatedStudent.getStudentId(), courseCode + classCode
            );
        } catch (StudentNotFoundException | OfferingNotFoundException | OfferingRecordNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void removeFromWeeklySchedule(HttpServletRequest request) {
        String classCode = request.getParameter("class_code");
        String courseCode = request.getParameter("course_code");
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

    private void resetWeeklySchedule() {
        try {
            WeeklyScheduleEntity weeklyScheduleEntity = model.getWeeklySchedule(authenticatedStudent.getStudentId());
            model.resetWeeklySchedule(weeklyScheduleEntity);
        } catch (OfferingRecordNotFoundException | StudentNotFoundException | OfferingCodeNotInWeeklyScheduleException e) {
            e.printStackTrace();
        }

    }

    private Boolean submitWeeklySchedule(HttpServletRequest request) {
        try {
            List<Exception> exceptionList = model.finalizeWeeklySchedule(authenticatedStudent.getStudentId());
            request.setAttribute("errorList", GetExceptionMessages.getExceptionMessages(exceptionList));
            return exceptionList.isEmpty();
        } catch (StudentNotFoundException | OfferingRecordNotFoundException | OfferingCodeNotInWeeklyScheduleException | OfferingNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setWeeklyScheduleOfferings(HttpServletRequest request) {
        try {
            WeeklyScheduleEntity weeklyScheduleEntity = model.getWeeklySchedule(authenticatedStudent.getStudentId());

            request.setAttribute(
                    "weeklyScheduleOfferings",
                    model.getWeeklyScheduleOfferingEntities(weeklyScheduleEntity)
            );
        } catch (StudentNotFoundException | OfferingNotFoundException | OfferingRecordNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setStudentData(HttpServletRequest request) {
        request.setAttribute("studentId", authenticatedStudent.getStudentId());

        try {
            request.setAttribute("units", model.calculateUnitsSum(authenticatedStudent.getStudentId()));
        } catch (StudentNotFoundException | OfferingNotFoundException | OfferingRecordNotFoundException e) {
            e.printStackTrace();
        }
    }

}