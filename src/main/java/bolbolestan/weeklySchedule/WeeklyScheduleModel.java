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

    public List<Exception> addToWeeklySchedule(String studentId, String offeringCode)
            throws StudentNotFoundException, OfferingNotFoundException, OfferingRecordNotFoundException {
        new StudentModel().getStudent(studentId);
        new OfferingModel().getOffering(offeringCode);
        List<Exception> exceptionList = new ArrayList<>();

        WeeklyScheduleEntity weeklyScheduleEntity;
        try {
            weeklyScheduleEntity = WeeklyScheduleStorage.getByStudentId(studentId);
        } catch (WeeklyScheduleDoesNotExistException e) {
            weeklyScheduleEntity = addNewWeeklySchedule(studentId);
        }

        if (!weeklyScheduleEntity.getOfferingCodes().contains(offeringCode)) {
            weeklyScheduleEntity.addToOfferingCodes(offeringCode);

            exceptionList = validateAddToWeeklySchedule(weeklyScheduleEntity);
            if (
                    exceptionList.isEmpty() &&
                    !(new OfferingRecordModel().getOfferingRecord(studentId, offeringCode)
                            .getStatus().equals(OfferingRecordEntity.COMPLETED_STATUS))) {
                new OfferingRecordModel().addNewOfferingRecord(
                        studentId, offeringCode, 0.0, OfferingRecordEntity.NON_FINALIZED_STATUS
                );
            } else  {
                try {
                    weeklyScheduleEntity.removeFromOfferingCodes(offeringCode);
                } catch (OfferingCodeNotInWeeklyScheduleException e) {
                    e.printStackTrace();
                }
            }
        } else if (new OfferingRecordModel().getOfferingRecord(studentId, offeringCode)
                .getStatus().equals(OfferingRecordEntity.REMOVED_STATUS)) {
            exceptionList = validateAddToWeeklySchedule(weeklyScheduleEntity);
            if (exceptionList.isEmpty()) {
                new OfferingRecordModel().updateStatusOfferingRecord(
                        studentId, offeringCode, OfferingRecordEntity.FINALIZED_STATUS);
            }
        }
        return exceptionList;
    }


    private void removeNonFinalizedOffering(String studentId, String offeringCode)
            throws OfferingCodeNotInWeeklyScheduleException{
        OfferingRecordModel offeringRecordModel = new OfferingRecordModel();
        try {
            WeeklyScheduleStorage.getByStudentId(studentId).removeFromOfferingCodes(offeringCode);
            offeringRecordModel.removeOfferingRecord(studentId, offeringCode);
        } catch (WeeklyScheduleDoesNotExistException | OfferingRecordNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void updateOfferingRecordStatusToRemove(String studentId, String offeringCode) {
        OfferingRecordModel offeringRecordModel = new OfferingRecordModel();
        try {
            offeringRecordModel.updateStatusOfferingRecord(studentId, offeringCode, OfferingRecordEntity.REMOVED_STATUS);
        } catch (OfferingRecordNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void removeFromWeeklySchedule(String studentId, String offeringCode) throws
            StudentNotFoundException,
            OfferingCodeNotInWeeklyScheduleException, OfferingRecordNotFoundException {
        new StudentModel().getStudent(studentId);
        OfferingRecordModel offeringRecordModel = new OfferingRecordModel();
        OfferingRecordEntity offeringRecordEntity = offeringRecordModel.getOfferingRecord(studentId, offeringCode);
        if (offeringRecordEntity.getStatus().equals(OfferingRecordEntity.NON_FINALIZED_STATUS)) {
            removeNonFinalizedOffering(studentId, offeringCode);
        } else {
            updateOfferingRecordStatusToRemove(studentId, offeringCode);
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

    private Boolean isBothNonDeleted(String studentId, String offeringCode1, String offeringCode2) {
        try {
            return isGoingToAdd(studentId, offeringCode1) && isGoingToAdd(studentId, offeringCode2);
        } catch (OfferingRecordNotFoundException e) {
            return true;
        }
    }

    private List<Exception> validateWeeklyScheduleCollision(WeeklyScheduleEntity weeklyScheduleEntity) {
        List<Exception> exceptionList = new ArrayList<>();
        List<String> offeringCodes = weeklyScheduleEntity.getOfferingCodes();
        String offeringCode1 = "";
        String offeringCode2 = "";
        for (int i = 0; i < offeringCodes.size(); i++) {
            try {
                offeringCode1 = offeringCodes.get(i);
                OfferingEntity offeringEntity1 = new OfferingModel().getOffering(offeringCode1);
                for (int j = i + 1; j < offeringCodes.size(); j++) {
                    offeringCode2 = offeringCodes.get(j);
                    OfferingEntity offeringEntity2 = new OfferingModel().getOffering(offeringCode2);
                    Boolean isBothNonDeleted =
                            isBothNonDeleted(weeklyScheduleEntity.getStudentId(), offeringCode1, offeringCode2);
                    if (isBothNonDeleted && this.doesOfferingsSessionsCollied(offeringEntity1, offeringEntity2)) {
                        exceptionList.add(new ClassCollisionException(offeringCode1, offeringCode2));
                    }
                    if (isBothNonDeleted && this.doesOfferingsExamsCollied(offeringEntity1, offeringEntity2)) {
                        exceptionList.add(new ExamTimeCollisionException(offeringCode1, offeringCode2));
                    }
                }
            } catch (OfferingNotFoundException e) {
                System.out.println("Nooooooooooooo2");
                e.printStackTrace();
                System.out.println("offeringCode1 = " + offeringCode1);
                System.out.println("offeringCode2 = " + offeringCode2);
            }
        }

        return exceptionList;
    }

    private List<Exception> validateWeeklyScheduleCapacity(WeeklyScheduleEntity weeklyScheduleEntity) {
        List<Exception> exceptionList = new ArrayList<>();
        List<String> offeringCodes = weeklyScheduleEntity.getOfferingCodes();
        for (String offeringCode: offeringCodes) {
            try {
                if (isGoingToAdd(weeklyScheduleEntity.getStudentId(), offeringCode)) {
                    OfferingEntity offeringEntity = new OfferingModel().getOffering(offeringCode);
                    if (!(new OfferingModel().doseHaveCapacity(offeringEntity))) {
                        exceptionList.add(new CapacityException(offeringCode));
                    }
                }
            } catch (OfferingNotFoundException | OfferingRecordNotFoundException e) {
                e.printStackTrace();
            }
        }
        return exceptionList;
    }

    private List<Exception> validateAddToWeeklySchedule(WeeklyScheduleEntity weeklyScheduleEntity) {
        List<Exception> exceptionList = new ArrayList<>();

        exceptionList.addAll(this.validateWeeklyScheduleCollision(weeklyScheduleEntity));
        return exceptionList;
    }

    private List<Exception> validateHasTakenBefore(WeeklyScheduleEntity weeklyScheduleEntity) throws OfferingRecordNotFoundException {
        List<Exception> exceptionList = new ArrayList<>();
        for (String offeringCode: weeklyScheduleEntity.getOfferingCodes()) {
            if (isGoingToAdd(weeklyScheduleEntity.getStudentId(), offeringCode)) {
                if (new OfferingRecordModel().isOfferingCompleted(weeklyScheduleEntity.getStudentId(), offeringCode)) {
                    exceptionList.add(new CourseHasTakenBeforeException(offeringCode));
                }
            }
        }
        return exceptionList;
    }

    private void validatePrerequisitesOfOffering(String studentId, String offeringCode)
            throws OfferingNotFoundException, PrerequisiteException {
        OfferingEntity offeringEntity = new OfferingModel().getOffering(offeringCode);
        StudentModel studentModel = new StudentModel();
        if (offeringEntity.getPrerequisites() == null) {
            return;
        }
        for (String prerequisite: offeringEntity.getPrerequisites()) {
            if (!(studentModel.hasPassedCourse(studentId, prerequisite))) {
                throw new PrerequisiteException(offeringCode);
            }
        }
    }

    private Boolean isGoingToAdd(String studentId, String offeringCode) throws OfferingRecordNotFoundException {
        String offeringRecordStatus =
                new OfferingRecordModel().getOfferingRecord(studentId, offeringCode).getStatus();
        return offeringRecordStatus.equals(OfferingRecordEntity.NON_FINALIZED_STATUS);
    }

    private List<Exception> validatePrerequisites(WeeklyScheduleEntity weeklyScheduleEntity)
            throws OfferingRecordNotFoundException, OfferingNotFoundException {
        List<Exception> exceptionList = new ArrayList<>();
        for (String offeringCode: weeklyScheduleEntity.getOfferingCodes()) {
            if (isGoingToAdd(weeklyScheduleEntity.getStudentId(), offeringCode)) {
                try {
                    validatePrerequisitesOfOffering(weeklyScheduleEntity.getStudentId(), offeringCode);
                } catch (PrerequisiteException e) {
                    exceptionList.add(e);
                }
            }
        }
        return exceptionList;
    }

    private List<Exception> validateFinalizeWeeklySchedule(WeeklyScheduleEntity weeklyScheduleEntity)
            throws OfferingRecordNotFoundException, OfferingNotFoundException {
        List<Exception> exceptionList = new ArrayList<>();
        try {
            this.validateUnitLimit(weeklyScheduleEntity);
        } catch (Exception e) {
            exceptionList.add(e);
        }

        exceptionList.addAll(this.validatePrerequisites(weeklyScheduleEntity));
        exceptionList.addAll(this.validateHasTakenBefore(weeklyScheduleEntity));
        exceptionList.addAll(this.validateWeeklyScheduleCapacity(weeklyScheduleEntity));
        return exceptionList;
    }

    public void addStudentToWeeklyScheduleOffering(String offeringCode) {
        try {
            OfferingEntity offeringEntity = new OfferingModel().getOffering(offeringCode);
            new OfferingModel().addStudentToOffering(offeringEntity);

        } catch (OfferingNotFoundException | CapacityMismatchException e) {
            e.printStackTrace();
        }
    }

    public void removeStudentFromWeeklyScheduleOffering(String offeringCode) {
        try {
            OfferingEntity offeringEntity = new OfferingModel().getOffering(offeringCode);
            new OfferingModel().removeStudentFromOffering(offeringEntity);
        } catch (OfferingNotFoundException | CapacityMismatchException e) {
            e.printStackTrace();
        }
    }

    private void finalizeOfferingRecord(String studentId, String offeringCode) throws OfferingRecordNotFoundException {
        OfferingRecordModel offeringRecordModel = new OfferingRecordModel();
        offeringRecordModel.updateStatusOfferingRecord(
                studentId, offeringCode, OfferingRecordEntity.FINALIZED_STATUS
        );
    }

    private void finalizeOfferings(WeeklyScheduleEntity weeklyScheduleEntity)
            throws OfferingRecordNotFoundException, OfferingCodeNotInWeeklyScheduleException {
        List<String> offeringCodes = copyArrayList(weeklyScheduleEntity.getOfferingCodes());
        OfferingRecordModel offeringRecordModel = new OfferingRecordModel();
        String studentId = weeklyScheduleEntity.getStudentId();
        for (String offeringCode: offeringCodes) {
            String offeringRecordStatus = offeringRecordModel.getOfferingRecord(studentId, offeringCode).getStatus();
            if (offeringRecordStatus.equals(OfferingRecordEntity.NON_FINALIZED_STATUS)) {
                addStudentToWeeklyScheduleOffering(offeringCode);
                finalizeOfferingRecord(studentId, offeringCode);
            } else if (offeringRecordStatus.equals(OfferingRecordEntity.REMOVED_STATUS)) {
                removeStudentFromWeeklyScheduleOffering(offeringCode);
                removeNonFinalizedOffering(studentId, offeringCode);
            }
        }
    }

    public List<Exception> finalizeWeeklySchedule(String studentId) throws
            StudentNotFoundException,
            OfferingRecordNotFoundException,
            OfferingCodeNotInWeeklyScheduleException,
            OfferingNotFoundException {
        new StudentModel().getStudent(studentId);
        WeeklyScheduleEntity weeklyScheduleEntity;
        try {
            weeklyScheduleEntity = WeeklyScheduleStorage.getByStudentId(studentId);
        } catch (WeeklyScheduleDoesNotExistException e) {
            weeklyScheduleEntity = addNewWeeklySchedule(studentId);
        }
        List<Exception> exceptionList = validateFinalizeWeeklySchedule(weeklyScheduleEntity);
        if (exceptionList.isEmpty()) {
            this.finalizeOfferings(weeklyScheduleEntity);
        }
        return exceptionList;
    }

    public List<String> classHours() {
        List<String> hoursList = new ArrayList<>();
        hoursList.add("07:30-09:00");
        hoursList.add("09:00-10:30");
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
                ((HashMap<String, String>) data.get(day.name())).put(classTimeRange, offeringEntity.getName());
            }
        }
        return data;
    }

    public Integer calculateUnitsSum(String studentId)
            throws StudentNotFoundException, OfferingNotFoundException, OfferingRecordNotFoundException {
        WeeklyScheduleEntity weeklyScheduleEntity = getWeeklySchedule(studentId);
        List<String> weeklyScheduleOfferingCodes = weeklyScheduleEntity.getOfferingCodes();
        int sum = 0;
        OfferingRecordModel offeringRecordModel = new OfferingRecordModel();

        for (String offeringCode: weeklyScheduleOfferingCodes) {
            String offeringRecordStatus = offeringRecordModel.getOfferingRecord(weeklyScheduleEntity.getStudentId(), offeringCode).getStatus();
            if (isShouldBeShown(offeringRecordStatus)) {
                OfferingEntity offeringEntity = new OfferingModel().getOffering(offeringCode);
                sum += offeringEntity.getUnits();
            }
        }
        return sum;
    }

    private boolean isShouldBeShown(String offeringRecordStatus) {
        return offeringRecordStatus.equals(OfferingRecordEntity.NON_FINALIZED_STATUS) ||
                offeringRecordStatus.equals(OfferingRecordEntity.FINALIZED_STATUS);
    }

    public List<OfferingEntity> getWeeklyScheduleOfferingEntities(WeeklyScheduleEntity weeklyScheduleEntity)
            throws OfferingNotFoundException, OfferingRecordNotFoundException {
        OfferingModel offeringModel = new OfferingModel();
        OfferingRecordModel offeringRecordModel = new OfferingRecordModel();
        List<OfferingEntity> offeringEntities = new ArrayList<>();
        for (String offeringCode : weeklyScheduleEntity.getOfferingCodes()) {
            String offeringRecordStatus =
                    offeringRecordModel.getOfferingRecord(
                            weeklyScheduleEntity.getStudentId(),
                            offeringCode
                    ).getStatus();
            if (isShouldBeShown(offeringRecordStatus)) {
                offeringEntities.add(offeringModel.getOffering(offeringCode));
            }
        }
        return offeringEntities;
    }

    public void resetWeeklySchedule(WeeklyScheduleEntity weeklyScheduleEntity)
            throws OfferingRecordNotFoundException, OfferingCodeNotInWeeklyScheduleException {
        OfferingRecordModel offeringRecordModel = new OfferingRecordModel();
        List<String> removeOfferingCodes = new ArrayList<>();
        for (String offeringCode: weeklyScheduleEntity.getOfferingCodes()) {
            OfferingRecordEntity offeringRecordEntity =
                    offeringRecordModel.getOfferingRecord(weeklyScheduleEntity.getStudentId(), offeringCode);
            if (offeringRecordEntity.getStatus().equals(OfferingRecordEntity.NON_FINALIZED_STATUS)) {
                removeOfferingCodes.add(offeringCode);
                offeringRecordModel.removeOfferingRecord(weeklyScheduleEntity.getStudentId(), offeringCode);
            }
        }

        for (String offeringCode: removeOfferingCodes) {
            weeklyScheduleEntity.removeFromOfferingCodes(offeringCode);
        }
    }

    private List<String> copyArrayList(List<String> inputArray) {
        return new ArrayList<>(inputArray);
    }

}
