package bolbolestan.weeklySchedule;

import bolbolestan.bolbolestanExceptions.OfferingCodeNotInWeeklyScheduleException;
import bolbolestan.bolbolestanExceptions.OfferingNotFoundException;
import bolbolestan.bolbolestanExceptions.OfferingRecordNotFoundException;
import bolbolestan.bolbolestanExceptions.StudentNotFoundException;
import bolbolestan.middlewares.Authentication;
import bolbolestan.offering.OfferingEntity;
import bolbolestan.offering.OfferingModel;
import bolbolestan.student.StudentEntity;

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

        if (authenticatedStudent != null) {
            this.setStudentData(request);
            this.setWeeklyScheduleOfferings(request);
            this.setOfferingEntities(request);
            request.getRequestDispatcher("/WEB-INF/courses.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/login/");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        switch (request.getParameter("action")) {
            case "submit":
                submitWeeklySchedule();
                response.sendRedirect(request.getContextPath() + "/courses/");
                //todo: weeklySchedule plan
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
            default:
                //
        }

    }

    private void setOfferingEntities(HttpServletRequest request) {
        List<OfferingEntity> offeringEntities = new OfferingModel().getOfferings();
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
        } catch (StudentNotFoundException | OfferingNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void removeFromWeeklySchedule(HttpServletRequest request) {
        String classCode = request.getParameter("class_code");
        String courseCode = request.getParameter("course_code");
        try {
            model.removeFromWeeklySchedule(authenticatedStudent.getStudentId(), courseCode + classCode);
        } catch (StudentNotFoundException | OfferingCodeNotInWeeklyScheduleException | OfferingRecordNotFoundException e) {
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

    private void submitWeeklySchedule() {
        try {
            model.finalizeWeeklySchedule(authenticatedStudent.getStudentId());
        } catch (StudentNotFoundException | OfferingRecordNotFoundException | OfferingCodeNotInWeeklyScheduleException e) {
            e.printStackTrace();
        }
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