package bolbolestan.weeklySchedule;

import bolbolestan.offering.OfferingModel;
import bolbolestan.student.StudentModel;

public class WeeklyScheduleModel {

    public WeeklyScheduleEntity addNewWeeklySchedule(String studentId) {
        WeeklyScheduleEntity weeklyScheduleEntity = new WeeklyScheduleEntity();
        weeklyScheduleEntity.setStudentId(studentId);
        weeklyScheduleEntity.setStatus(WeeklyScheduleEntity.NON_FINALIZED_STATUS);
        WeeklyScheduleStorage.add(weeklyScheduleEntity);
        return weeklyScheduleEntity;
    }

    public void addToWeeklySchedule(String studentId, String offeringCode) {
        new StudentModel().getStudent(studentId); //todo: is valid, exception
        new OfferingModel().getOffering(offeringCode); //todo: is valid, exception

        WeeklyScheduleEntity weeklyScheduleEntity;
        try {
            weeklyScheduleEntity = WeeklyScheduleStorage.getByStudentId(studentId);
        } catch (Exception e) {
            weeklyScheduleEntity = addNewWeeklySchedule(studentId);
            e.printStackTrace(); //todo: proper exception
        }
        weeklyScheduleEntity.addToOfferingCodes(offeringCode);
    }

    public void removeFromWeeklySchedule(String studentId, String offeringCode) {
        new StudentModel().getStudent(studentId); //todo: is valid, exception
        try {
            WeeklyScheduleStorage.getByStudentId(studentId).removeFromOfferingCodes(offeringCode);
        } catch (Exception e) {
            e.printStackTrace(); //todo: proper exception 1 and 2
        }
    }

    public WeeklyScheduleEntity getWeeklySchedule(String studentId) {
        new StudentModel().getStudent(studentId); //todo: is valid, exception

        try {
            return WeeklyScheduleStorage.getByStudentId(studentId);
        } catch (Exception e) {
            e.printStackTrace(); //todo: proper exception
        }
        return null;
    }

    public void finalizeWeeklySchedule(String studentId) {
        //todo :
    }
}
