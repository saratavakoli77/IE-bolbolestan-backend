package bolbolestan.weeklySchedule;

import bolbolestan.bolbolestanExceptions.*;
import bolbolestan.course.CourseEntity;
import bolbolestan.course.DaysOfWeek;
import bolbolestan.offering.OfferingEntity;
import bolbolestan.offering.OfferingModel;
import bolbolestan.offeringRecord.OfferingRecordEntity;
import bolbolestan.offeringRecord.OfferingRecordModel;
import bolbolestan.student.StudentModel;
import bolbolestan.tools.DateParser;
import bolbolestan.tools.DateParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WeeklyScheduleModel {
    public WeeklyScheduleEntity addNewWeeklySchedule(String studentId) {
        WeeklyScheduleEntity weeklyScheduleEntity = new WeeklyScheduleEntity();
        weeklyScheduleEntity.setStudentId(studentId);
        weeklyScheduleEntity.setStatus(WeeklyScheduleEntity.NON_FINALIZED_STATUS);
        WeeklyScheduleStorage.add(weeklyScheduleEntity);
        return weeklyScheduleEntity;
    }

    public List<Exception> addToWeeklySchedule(String studentId, String offeringCode) throws StudentNotFoundException, OfferingNotFoundException {
        new StudentModel().getStudent(studentId);
        new OfferingModel().getOffering(offeringCode);

        WeeklyScheduleEntity weeklyScheduleEntity;
        try {
            weeklyScheduleEntity = WeeklyScheduleStorage.getByStudentId(studentId);
        } catch (WeeklyScheduleDoesNotExistException e) {
            weeklyScheduleEntity = addNewWeeklySchedule(studentId);
        }
        List<Exception> exceptionList = validateAddToWeeklySchedule(weeklyScheduleEntity);
        if (exceptionList.isEmpty()) {
            weeklyScheduleEntity.addToOfferingCodes(offeringCode);
            new OfferingRecordModel().addNewOfferingRecord(
                    studentId, offeringCode, 0.0, OfferingRecordEntity.NON_FINALIZED_STATUS
            );
        }
        return exceptionList;
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
        if (unitsCount < CourseEntity.MINIMUM_UNITS_LIMIT) {
            throw new MinimumUnitsException();
        } else if (unitsCount > CourseEntity.MAXIMUM_UNITS_LIMIT) {
            throw new MaximumUnitsException();
        }
        return true;
    }

    private Boolean doesOfferingsSessionsCollied(OfferingEntity offeringEntity1, OfferingEntity offeringEntity2) {
        List<DaysOfWeek> classTimeDays1 = offeringEntity1.getClassTimeDays();
        List<DaysOfWeek> classTimeDays2 = offeringEntity2.getClassTimeDays();

        for (DaysOfWeek dayOfWeek: classTimeDays1) {
            if (classTimeDays2.contains(dayOfWeek)) {
                if (DateParser.doseDatesCollide(
                        offeringEntity1.getClassTimeStart(),
                        offeringEntity1.getClassTimeEnd(),
                        offeringEntity2.getClassTimeStart(),
                        offeringEntity2.getClassTimeEnd()
                )) {
                    return true;
                }
            }
        }
        return false;
    }


    private Boolean doesOfferingsExamsCollied(OfferingEntity offeringEntity1, OfferingEntity offeringEntity2) {
        return DateParser.doseDatesCollide(
                offeringEntity1.getExamTimeStart(),
                offeringEntity1.getExamTimeEnd(),
                offeringEntity2.getExamTimeStart(),
                offeringEntity2.getExamTimeEnd()
        );
    }

    private List<Exception> validateWeeklyScheduleCollision(WeeklyScheduleEntity weeklyScheduleEntity) {
        List<Exception> exceptionList = new ArrayList<>();
        List<String> offeringCodes = weeklyScheduleEntity.getOfferingCodes();
        for (int i = 0; i < offeringCodes.size(); i++) {
            try {
                String offeringCode1 = offeringCodes.get(i);
                OfferingEntity offeringEntity1 = new OfferingModel().getOffering(offeringCode1);
                for (int j = i + 1; j < offeringCodes.size(); j++) {
                    String offeringCode2 = offeringCodes.get(j);
                    OfferingEntity offeringEntity2 = new OfferingModel().getOffering(offeringCode2);
                    if (this.doesOfferingsSessionsCollied(offeringEntity1, offeringEntity2)) {
                        exceptionList.add(new ClassCollisionException(offeringCode1, offeringCode2));
                    }
                    if (this.doesOfferingsExamsCollied(offeringEntity1, offeringEntity2)) {
                        exceptionList.add(new ExamTimeCollisionException(offeringCode1, offeringCode2));
                    }
                }
            } catch (OfferingNotFoundException e) {
                System.out.println("Nooooooooooooo");
            }
        }

        return exceptionList;
    }

    private List<Exception> validateWeeklyScheduleCapacity(WeeklyScheduleEntity weeklyScheduleEntity) {
        List<Exception> exceptionList = new ArrayList<>();
        List<String> offeringCodes = weeklyScheduleEntity.getOfferingCodes();
        for (String offeringCode: offeringCodes) {
            try {
                OfferingEntity offeringEntity = new OfferingModel().getOffering(offeringCode);
                if (!(new OfferingModel().doseHaveCapacity(offeringEntity))) {
                    exceptionList.add(new CapacityException(offeringCode));
                }
            } catch (OfferingNotFoundException e) {
                e.printStackTrace();
            }
        }
        return exceptionList;
    }

    private List<Exception> validateAddToWeeklySchedule(WeeklyScheduleEntity weeklyScheduleEntity) {
        List<Exception> exceptionList = new ArrayList<>();
        exceptionList.addAll(this.validateWeeklyScheduleCollision(weeklyScheduleEntity));
        exceptionList.addAll(this.validateWeeklyScheduleCapacity(weeklyScheduleEntity));
        return exceptionList;
    }

    private List<Exception> validateFinalizeWeeklySchedule(WeeklyScheduleEntity weeklyScheduleEntity) {
        List<Exception> exceptionList = new ArrayList<>();
        try {
            this.validateUnitLimit(weeklyScheduleEntity);
        } catch (Exception e) {
            exceptionList.add(e);
        }
        return exceptionList;
    }

    public void addStudentToWeeklyScheduleOfferings(WeeklyScheduleEntity weeklyScheduleEntity) {
        List<String> offeringCodes = weeklyScheduleEntity.getOfferingCodes();
        for (String offeringCode: offeringCodes) {
            try {
                OfferingEntity offeringEntity = new OfferingModel().getOffering(offeringCode);
                new OfferingModel().addStudentToOffering(offeringEntity);
            } catch (OfferingNotFoundException | CapacityMismatchException e) {
                e.printStackTrace();
            }
        }
    }

    private void finalizeOfferings(WeeklyScheduleEntity weeklyScheduleEntity) throws OfferingRecordNotFoundException {
        List<String> offeringCodes = weeklyScheduleEntity.getOfferingCodes();
        OfferingRecordModel offeringRecordModel = new OfferingRecordModel();
        for (String offeringCode: offeringCodes) {
            offeringRecordModel.updateStatusOfferingRecord(
                    weeklyScheduleEntity.getStudentId(), offeringCode, OfferingRecordEntity.FINALIZED_STATUS
            );
        }
    }

    public List<Exception> finalizeWeeklySchedule(String studentId) throws
            StudentNotFoundException, OfferingRecordNotFoundException {
        new StudentModel().getStudent(studentId);
        WeeklyScheduleEntity weeklyScheduleEntity;
        try {
            weeklyScheduleEntity = WeeklyScheduleStorage.getByStudentId(studentId);
        } catch (WeeklyScheduleDoesNotExistException e) {
            weeklyScheduleEntity = addNewWeeklySchedule(studentId);
        }
        List<Exception> exceptionList = validateFinalizeWeeklySchedule(weeklyScheduleEntity);
        if (exceptionList.isEmpty()) {
            this.addStudentToWeeklyScheduleOfferings(weeklyScheduleEntity);
            this.finalizeOfferings(weeklyScheduleEntity);
        }
        return exceptionList;
    }

    private List<String> classHours() {
        List<String> hoursList = new ArrayList<>();
        hoursList.add("7:30-9:00");
        hoursList.add("9:00-10:30");
        hoursList.add("10:30-12:00");
        hoursList.add("14:00-15:30");
        hoursList.add("16:00-17:30");
        return hoursList;
    }

    private HashMap<String, Object> initWeekDaysMap() {
        HashMap<String, Object> data = new HashMap<>();

        List<String> classHours = classHours();

        for (DaysOfWeek daysOfWeek: DaysOfWeek.values()) {
            HashMap<String, String> hours = new HashMap<>();
            for (String hour: classHours) {
                hours.put(hour, "");
            }
            data.put(daysOfWeek.name(), hours);
        }
        return data;
    }

    public HashMap<String, Object> getWeeklySchedulePlan(String studentId) throws
            StudentNotFoundException, OfferingNotFoundException {
        HashMap<String, Object> data = initWeekDaysMap();
        WeeklyScheduleEntity weeklyScheduleEntity = getWeeklySchedule(studentId);
        List<String> weeklyScheduleOfferingCodes = weeklyScheduleEntity.getOfferingCodes();
        for (String offeringCode: weeklyScheduleOfferingCodes) {
            OfferingEntity offeringEntity = new OfferingModel().getOffering(offeringCode);
            for (DaysOfWeek day: offeringEntity.getClassTimeDays()) {
                String classTimeRange = DateParser.getStringFromDates(
                        offeringEntity.getClassTimeStart(),
                        offeringEntity.getClassTimeEnd()
                );
//                HashMap<String, String> dayMap = (HashMap<String, String>) data.get(day.name());
//                dayMap.put(classTimeRange, offeringEntity.getName());
                ((HashMap<String, String>) data.get(day.name())).put(classTimeRange, offeringEntity.getName());
            }
        }
        return data;
    }

}
