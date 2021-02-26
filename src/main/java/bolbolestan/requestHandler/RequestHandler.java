package bolbolestan.requestHandler;

import bolbolestan.offering.OfferingEntity;
import bolbolestan.offering.OfferingModel;
import bolbolestan.student.StudentEntity;

public class RequestHandler {
    public void finalizeSchedule(String studentId) {
    }

    public void getWeeklySchedule(String studentId) {
    }

    public void removeFromWeeklySchedule(String studentId, String offeringCode) {
    }

    public void addToWeeklySchedule(String studentId, String offeringCode) {
    }

    public void getOfferings(String studentId) {
    }

    public void addStudent(StudentEntity studentEntity) {

    }

    public void addOffering(OfferingEntity offeringEntity) {
        new OfferingModel().addNewOffering(offeringEntity);
    }

}
