package bolbolestan.requestHandler;

import bolbolestan.bolbolestanExceptions.OfferingCodeNotInWeeklyScheduleException;
import bolbolestan.bolbolestanExceptions.OfferingNotFoundException;
import bolbolestan.bolbolestanExceptions.StudentNotFoundException;
import bolbolestan.offering.OfferingEntity;
import bolbolestan.offering.OfferingModel;
import bolbolestan.student.StudentEntity;
import bolbolestan.student.StudentModel;
import bolbolestan.tools.GetExceptionMessages;
import bolbolestan.weeklySchedule.WeeklyScheduleEntity;
import bolbolestan.weeklySchedule.WeeklyScheduleModel;

import java.util.HashMap;
import java.util.List;

public class RequestHandler {
    public HashMap<String, Object> finalizeSchedule(String studentId) {
        HashMap<String, Object> response = new HashMap<>();
        List<Exception> exceptionList;
        try {
            exceptionList = new WeeklyScheduleModel().finalizeWeeklySchedule(studentId);
            if (exceptionList.isEmpty()) {
                response.put("success", true);
                response.put("data", null);
            } else {
                response.put("success", false);
                response.put("error", GetExceptionMessages.getExceptionMessages(exceptionList));
            }
        } catch (StudentNotFoundException e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }

        return response;
    }

    public HashMap<String, Object> getWeeklySchedule(String studentId) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            WeeklyScheduleEntity weeklyScheduleEntity = new WeeklyScheduleModel().getWeeklySchedule(studentId);
            response.put("success", true);
            response.put("data", weeklyScheduleEntity);
        } catch (StudentNotFoundException e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return response;
    }

    public HashMap<String, Object> removeFromWeeklySchedule(String studentId, String offeringCode) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            new WeeklyScheduleModel().removeFromWeeklySchedule(studentId, offeringCode);
            response.put("success", true);
            response.put("data", null);
        } catch (StudentNotFoundException | OfferingCodeNotInWeeklyScheduleException e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return response;
    }

    public HashMap<String, Object> addToWeeklySchedule(String studentId, String offeringCode) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            new WeeklyScheduleModel().addToWeeklySchedule(studentId, offeringCode);
            response.put("success", true);
            response.put("data", null);
        } catch (StudentNotFoundException | OfferingNotFoundException e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return response;
    }

    public HashMap<String, Object> getOffering(String studentId, String offeringCode) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            new StudentModel().getStudent(studentId);
            OfferingEntity offeringEntity = new OfferingModel().getOffering(offeringCode);
            response.put("success", true);
            response.put("data", offeringEntity);
        } catch (OfferingNotFoundException | StudentNotFoundException e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return response;
    }

    public HashMap<String, Object> getOfferings(String studentId) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            new StudentModel().getStudent(studentId);
            List<OfferingEntity> offeringEntities = new OfferingModel().getOfferings();
            response.put("success", true);
            response.put("data", offeringEntities);
        } catch (StudentNotFoundException e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return response;
    }

    public HashMap<String, Object> addStudent(StudentEntity studentEntity) {
        new StudentModel().addNewStudent(studentEntity);
        HashMap<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", null);
        return response;
    }

    public HashMap<String, Object> addOffering(OfferingEntity offeringEntity) {
        new OfferingModel().addNewOffering(offeringEntity);
        HashMap<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", null);
        return response;
    }

}
