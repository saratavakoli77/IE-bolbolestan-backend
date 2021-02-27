package bolbolestan.weeklySchedule;

import bolbolestan.bolbolestanExceptions.*;
import bolbolestan.offering.OfferingEntity;
import bolbolestan.offering.OfferingModel;
import bolbolestan.student.StudentModel;

import java.util.ArrayList;
import java.util.List;

public class WeeklyScheduleModel {

    public WeeklyScheduleEntity addNewWeeklySchedule(String studentId) {
        WeeklyScheduleEntity weeklyScheduleEntity = new WeeklyScheduleEntity();
        weeklyScheduleEntity.setStudentId(studentId);
        weeklyScheduleEntity.setStatus(WeeklyScheduleEntity.NON_FINALIZED_STATUS);
        WeeklyScheduleStorage.add(weeklyScheduleEntity);
        return weeklyScheduleEntity;
    }

    public void addToWeeklySchedule(String studentId, String offeringCode) throws StudentNotFoundException, OfferingNotFoundException {
        new StudentModel().getStudent(studentId);
        new OfferingModel().getOffering(offeringCode);

        WeeklyScheduleEntity weeklyScheduleEntity;
        try {
            weeklyScheduleEntity = WeeklyScheduleStorage.getByStudentId(studentId);
        } catch (WeeklyScheduleDoesNotExistException e) {
            weeklyScheduleEntity = addNewWeeklySchedule(studentId);
        }
        weeklyScheduleEntity.addToOfferingCodes(offeringCode);
    }

    public void removeFromWeeklySchedule(String studentId, String offeringCode) throws
            StudentNotFoundException,
            OfferingCodeNotInWeeklyScheduleException {
        new StudentModel().getStudent(studentId);
        try {
            WeeklyScheduleStorage.getByStudentId(studentId).removeFromOfferingCodes(offeringCode);
        } catch (WeeklyScheduleDoesNotExistException e) {
            throw new OfferingCodeNotInWeeklyScheduleException();
        }
    }

    public WeeklyScheduleEntity getWeeklySchedule(String studentId) throws StudentNotFoundException {
        new StudentModel().getStudent(studentId);

        try {
            return WeeklyScheduleStorage.getByStudentId(studentId);
        } catch (WeeklyScheduleDoesNotExistException e) {
            return addNewWeeklySchedule(studentId);
        }
    }

    private Boolean validateUnitLimit(WeeklyScheduleEntity weeklyScheduleEntity) throws MinimumUnitsException, MaximumUnitsException {
        int unitsCount = 0;
        for (String offeringCode: weeklyScheduleEntity.getOfferingCodes()) {
            try {
                OfferingEntity offeringEntity = new OfferingModel().getOffering(offeringCode);
                unitsCount += offeringEntity.getUnits();
            } catch (OfferingNotFoundException e) {
                System.out.println("Nooooooooooooo");
            }
        }
        if (unitsCount < 12) {
            throw new MinimumUnitsException();
        } else if (unitsCount > 20) {
            throw new MaximumUnitsException();
        }
        return true;
    }

//    private List<Exception> validateWeeklyScheduleCollision(WeeklyScheduleEntity weeklyScheduleEntity) {
//        List<Exception> exceptionList = new ArrayList<>();
//        for (String offeringCode1: weeklyScheduleEntity.getOfferingCodes()) {
//            try {
//                OfferingEntity offeringEntity1 = new OfferingModel().getOffering(offeringCode1);
//                for (String offeringCode2: weeklyScheduleEntity.getOfferingCodes()) {
//                    OfferingEntity offeringEntity2 = new OfferingModel().getOffering(offeringCode2);
//
//                }
//
//
//            } catch (OfferingNotFoundException e) {
//                System.out.println("Nooooooooooooo");
//            }
//        }
//    }

    private List<Exception> validateWeeklySchedule(WeeklyScheduleEntity weeklyScheduleEntity) {
        List<Exception> exceptionList = new ArrayList<>();
        try {
            this.validateUnitLimit(weeklyScheduleEntity);
        } catch (Exception e) {
            exceptionList.add(e);
        }
        return exceptionList;
    }

    public void finalizeWeeklySchedule(String studentId) throws StudentNotFoundException {
        new StudentModel().getStudent(studentId);
        WeeklyScheduleEntity weeklyScheduleEntity;
        try {
            weeklyScheduleEntity = WeeklyScheduleStorage.getByStudentId(studentId);
        } catch (WeeklyScheduleDoesNotExistException e) {
            weeklyScheduleEntity = addNewWeeklySchedule(studentId);
        }

    }
}
