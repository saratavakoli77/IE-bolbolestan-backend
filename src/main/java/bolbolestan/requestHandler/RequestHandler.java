package bolbolestan.requestHandler;

import bolbolestan.bolbolestanExceptions.OfferingCodeNotInWeeklyScheduleException;
import bolbolestan.bolbolestanExceptions.OfferingNotFoundException;
import bolbolestan.bolbolestanExceptions.StudentNotFoundException;
import bolbolestan.offering.OfferingEntity;
import bolbolestan.offering.OfferingModel;
import bolbolestan.student.StudentEntity;
import bolbolestan.student.StudentModel;
import bolbolestan.weeklySchedule.WeeklyScheduleModel;

import java.util.List;

public class RequestHandler {
    public void finalizeSchedule(String studentId) {
        try {
            new WeeklyScheduleModel().finalizeWeeklySchedule(studentId);
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void getWeeklySchedule(String studentId) {
        try {
            new WeeklyScheduleModel().getWeeklySchedule(studentId);
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void removeFromWeeklySchedule(String studentId, String offeringCode) {
        try {
            new WeeklyScheduleModel().removeFromWeeklySchedule(studentId, offeringCode);
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        } catch (OfferingCodeNotInWeeklyScheduleException e) {
            e.printStackTrace();
        }
    }

    public void addToWeeklySchedule(String studentId, String offeringCode) {
        try {
            new WeeklyScheduleModel().addToWeeklySchedule(studentId, offeringCode);
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        } catch (OfferingNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<OfferingEntity> getOfferings(String studentId) {
        try {
            return new OfferingModel().getOfferings(studentId);
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addStudent(StudentEntity studentEntity) {
        new StudentModel().addStudent(studentEntity);
    }

    public void addOffering(OfferingEntity offeringEntity) {
        new OfferingModel().addNewOffering(offeringEntity);
    }

}
