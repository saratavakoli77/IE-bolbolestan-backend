package bolbolestan.weeklySchedule;

import bolbolestan.bolbolestanExceptions.OfferingNotFoundException;
import bolbolestan.bolbolestanExceptions.StudentNotFoundException;
import bolbolestan.offering.OfferingEntity;
import bolbolestan.offering.OfferingModel;

import java.util.HashMap;

import static java.lang.String.valueOf;

public class WeeklyScheduleView {
    private WeeklyScheduleModel model;

    public WeeklyScheduleView() {
        model = new WeeklyScheduleModel();
    }

    public HashMap<String, Object> getStudentWeeklySchedule(String studentId) throws
            OfferingNotFoundException, StudentNotFoundException {
        return model.getWeeklySchedulePlan(studentId);
    }

    public HashMap<String, Object> getWeeklyScheduleOfferings(String studentId)
            throws StudentNotFoundException, OfferingNotFoundException {
        WeeklyScheduleEntity weeklyScheduleEntity = model.getWeeklySchedule(studentId);
        HashMap<String, Object> data = new HashMap<>();

        OfferingModel offeringModel = new OfferingModel();
        for (String offeringCode: weeklyScheduleEntity.getOfferingCodes()) {
            OfferingEntity offeringEntity = offeringModel.getOffering(offeringCode);
            HashMap<String, String> offeringData = new HashMap<>();
            offeringData.put("courseCode", offeringEntity.getCode());
            offeringData.put("classCode", offeringEntity.getClassCode());
            offeringData.put("name", offeringEntity.getName());
            offeringData.put("units", valueOf(offeringEntity.getUnits()));
            data.put(offeringCode, offeringData);
        }

        return data;
    }
}
