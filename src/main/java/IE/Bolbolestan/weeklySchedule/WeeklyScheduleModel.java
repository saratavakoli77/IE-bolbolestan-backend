package IE.Bolbolestan.weeklySchedule;

import IE.Bolbolestan.bolbolestanExceptions.*;
import IE.Bolbolestan.course.CourseEntity;
import IE.Bolbolestan.course.DaysOfWeek;
import IE.Bolbolestan.offering.OfferingEntity;
import IE.Bolbolestan.offering.OfferingModel;
import IE.Bolbolestan.offeringRecord.OfferingRecordEntity;
import IE.Bolbolestan.offeringRecord.OfferingRecordModel;
import IE.Bolbolestan.student.StudentModel;
import IE.Bolbolestan.tools.DateParser;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WeeklyScheduleModel {
    public WeeklyScheduleEntity addNewWeeklySchedule(String studentId) {
        WeeklyScheduleEntity weeklyScheduleEntity = new WeeklyScheduleEntity();
        weeklyScheduleEntity.setStudentId(studentId);
        weeklyScheduleEntity.setStatus(WeeklyScheduleEntity.NON_FINALIZED_STATUS);
        try {
            WeeklyScheduleRepository.getInstance().insert(weeklyScheduleEntity);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return weeklyScheduleEntity;
    }

    public List<Exception> addToWeeklySchedule(String studentId, String offeringCode)
            throws StudentNotFoundException, OfferingNotFoundException, OfferingRecordNotFoundException {
        new StudentModel().getStudent(studentId);
        new OfferingModel().getOffering(offeringCode);
        OfferingRecordModel offeringRecordModel = new OfferingRecordModel();
        List<Exception> exceptionList = new ArrayList<>();

        WeeklyScheduleEntity weeklyScheduleEntity;
        try {
            weeklyScheduleEntity = WeeklyScheduleRepository.getInstance().getByStudentId(studentId);
            if (weeklyScheduleEntity == null) {
                weeklyScheduleEntity = addNewWeeklySchedule(studentId);
            }
        } catch (SQLException e) {
            weeklyScheduleEntity = addNewWeeklySchedule(studentId);
        }

        if (!weeklyScheduleEntity.getOfferingCodes().contains(offeringCode)) {
            weeklyScheduleEntity.addToOfferingCodes(offeringCode);
//            try {
//                WeeklyScheduleOfferingRepository.getInstance().insert(new WeeklyScheduleOfferingEntity(offeringCode, studentId));
//            } catch (SQLException throwables) {
//                throwables.printStackTrace();
//            }

            exceptionList = validateAddToWeeklySchedule(weeklyScheduleEntity);
            // if exist => not completed_status === not exist || not completed_status
            if (
                    exceptionList.isEmpty() && (!offeringRecordModel.doesOfferingRecordExist(studentId, offeringCode) ||
                            !(offeringRecordModel.getOfferingRecord(studentId, offeringCode)
                                    .getStatus().equals(OfferingRecordEntity.COMPLETED_STATUS)))
            ) {
                OfferingEntity offeringEntity = new OfferingModel().getOffering(offeringCode);
                if (offeringEntity.getRegistered() < offeringEntity.getCapacity()) {
                    new OfferingRecordModel().addNewOfferingRecord(
                            studentId, offeringCode, 0.0, OfferingRecordEntity.NON_FINALIZED_STATUS
                    );
                } else {
                    new OfferingRecordModel().addNewOfferingRecord(
                            studentId, offeringCode, 0.0, OfferingRecordEntity.NON_FINALIZED_WAIT
                    );
                }
            } else if (!exceptionList.isEmpty())  {
                try {
                    weeklyScheduleEntity.removeFromOfferingCodes(offeringCode);
                } catch (OfferingCodeNotInWeeklyScheduleException e) {
                    e.printStackTrace();
                }
            }
        } else if (offeringRecordModel.getOfferingRecord(studentId, offeringCode)
                .getStatus().equals(OfferingRecordEntity.REMOVED_STATUS)) {
            exceptionList = validateAddToWeeklySchedule(weeklyScheduleEntity);
            if (exceptionList.isEmpty()) {
                offeringRecordModel.updateStatusOfferingRecord(
                        studentId, offeringCode, OfferingRecordEntity.FINALIZED_STATUS);
            }
        } else if (offeringRecordModel.getOfferingRecord(studentId, offeringCode)
                .getStatus().equals(OfferingRecordEntity.REMOVED_WAIT)) {
            exceptionList = validateAddToWeeklySchedule(weeklyScheduleEntity);
            if (exceptionList.isEmpty()) {
                offeringRecordModel.updateStatusOfferingRecord(
                        studentId, offeringCode, OfferingRecordEntity.FINALIZED_WAIT);
            }
        }
        return exceptionList;
    }


    private void removeNonFinalizedOffering(String studentId, String offeringCode)
            throws OfferingCodeNotInWeeklyScheduleException{
        OfferingRecordModel offeringRecordModel = new OfferingRecordModel();
        try {
            WeeklyScheduleRepository.getInstance().getByStudentId(studentId).removeFromOfferingCodes(offeringCode);
            offeringRecordModel.removeOfferingRecord(studentId, offeringCode);
        } catch (OfferingRecordNotFoundException | SQLException e) {
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


    private void updateOfferingRecordStatusToRemoveWait(String studentId, String offeringCode) {
        OfferingRecordModel offeringRecordModel = new OfferingRecordModel();
        try {
            offeringRecordModel.updateStatusOfferingRecord(studentId, offeringCode, OfferingRecordEntity.REMOVED_WAIT);
        } catch (OfferingRecordNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void removeFromWeeklySchedule(String studentId, String offeringCode) throws
            StudentNotFoundException,
            OfferingCodeNotInWeeklyScheduleException,
            OfferingRecordNotFoundException,
            WeeklyScheduleDoesNotExistException,
            SQLException {
        new StudentModel().getStudent(studentId);
        OfferingRecordModel offeringRecordModel = new OfferingRecordModel();
        OfferingRecordEntity offeringRecordEntity = offeringRecordModel.getOfferingRecord(studentId, offeringCode);
        String status = offeringRecordEntity.getStatus();
        if (
                status.equals(OfferingRecordEntity.NON_FINALIZED_STATUS) ||
                        status.equals(OfferingRecordEntity.NON_FINALIZED_WAIT)
        ) {
            removeNonFinalizedOffering(studentId, offeringCode);
        } else if (status.equals(OfferingRecordEntity.REMOVED_STATUS)) {
            updateOfferingRecordStatusToRemove(studentId, offeringCode);
        } else if (status.equals(OfferingRecordEntity.COMPLETED_STATUS)) {
            WeeklyScheduleRepository.getInstance().getByStudentId(studentId).removeFromOfferingCodes(offeringCode);
        } else if (status.equals(OfferingRecordEntity.FINALIZED_STATUS)) {
            updateOfferingRecordStatusToRemove(studentId, offeringCode);
        } else if (status.equals(OfferingRecordEntity.FINALIZED_WAIT)) {
            updateOfferingRecordStatusToRemoveWait(studentId, offeringCode);
        }
    }

    public WeeklyScheduleEntity getWeeklySchedule(String studentId) throws StudentNotFoundException {
        new StudentModel().getStudent(studentId);
        try {
            WeeklyScheduleEntity weeklyScheduleEntity = WeeklyScheduleRepository.getInstance().getByStudentId(studentId);
            if (weeklyScheduleEntity != null) {
                return WeeklyScheduleRepository.getInstance().getByStudentId(studentId);
            }
            return addNewWeeklySchedule(studentId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Boolean validateUnitLimit(WeeklyScheduleEntity weeklyScheduleEntity) throws MinimumUnitsException, MaximumUnitsException {
        int unitsCount = 0;
        for (String offeringCode: weeklyScheduleEntity.getOfferingCodes()) {
            try {
                OfferingEntity offeringEntity = new OfferingModel().getOffering(offeringCode);
                if (
                        isGoingToAdd(weeklyScheduleEntity.getStudentId(), offeringCode) ||
                                isAddedBefore(weeklyScheduleEntity.getStudentId(), offeringCode) ||
                                new OfferingRecordModel().isOfferingCompleted(weeklyScheduleEntity.getStudentId(), offeringCode)
                ) {
                    unitsCount += offeringEntity.getUnits();
                }
            } catch (OfferingNotFoundException | OfferingRecordNotFoundException | SQLException e) {
                e.printStackTrace();
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
            OfferingRecordModel offeringRecordModel = new OfferingRecordModel();
            String status1 = offeringRecordModel.getOfferingRecord(studentId, offeringCode1).getStatus();
            String status2 = offeringRecordModel.getOfferingRecord(studentId, offeringCode2).getStatus();
            return !status1.equals(OfferingRecordEntity.REMOVED_STATUS) &&
                    !status2.equals(OfferingRecordEntity.REMOVED_STATUS) &&
                    !status1.equals(OfferingRecordEntity.REMOVED_WAIT) &&
                    !status2.equals(OfferingRecordEntity.REMOVED_WAIT);
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

    private List<Exception> validateHasTakenBefore(WeeklyScheduleEntity weeklyScheduleEntity) throws OfferingRecordNotFoundException, SQLException {
        List<Exception> exceptionList = new ArrayList<>();
        for (String offeringCode: weeklyScheduleEntity.getOfferingCodes()) {
            if (new OfferingRecordModel().isOfferingCompleted(weeklyScheduleEntity.getStudentId(), offeringCode)) {
                exceptionList.add(new CourseHasTakenBeforeException(offeringCode));
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
            if (!(studentModel.hasPassedCourse(studentId, prerequisite + "01"))) {
                throw new PrerequisiteException(offeringCode);
            }
        }
    }

    private Boolean isGoingToAdd(String studentId, String offeringCode) throws OfferingRecordNotFoundException {
        String offeringRecordStatus =
                new OfferingRecordModel().getOfferingRecord(studentId, offeringCode).getStatus();
        return offeringRecordStatus.equals(OfferingRecordEntity.NON_FINALIZED_STATUS);
    }

    private Boolean isAddedBefore(String studentId, String offeringCode) throws OfferingRecordNotFoundException {
        String offeringRecordStatus =
                new OfferingRecordModel().getOfferingRecord(studentId, offeringCode).getStatus();
        return offeringRecordStatus.equals(OfferingRecordEntity.FINALIZED_STATUS);
    }

    private Boolean isGoingToWait(String studentId, String offeringCode) throws OfferingRecordNotFoundException {
        String offeringRecordStatus =
                new OfferingRecordModel().getOfferingRecord(studentId, offeringCode).getStatus();
        return offeringRecordStatus.equals(OfferingRecordEntity.NON_FINALIZED_WAIT);
    }

    private List<Exception> validatePrerequisites(WeeklyScheduleEntity weeklyScheduleEntity)
            throws OfferingRecordNotFoundException, OfferingNotFoundException {
        List<Exception> exceptionList = new ArrayList<>();
        for (String offeringCode: weeklyScheduleEntity.getOfferingCodes()) {
            if (
                    isGoingToAdd(weeklyScheduleEntity.getStudentId(), offeringCode) ||
                            isGoingToWait(weeklyScheduleEntity.getStudentId(), offeringCode)
            ) {
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
            throws OfferingRecordNotFoundException, OfferingNotFoundException, SQLException {
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

        } catch (OfferingNotFoundException | CapacityMismatchException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeStudentFromWeeklyScheduleOffering(String offeringCode) {
        try {
            OfferingEntity offeringEntity = new OfferingModel().getOffering(offeringCode);
            new OfferingModel().removeStudentFromOffering(offeringEntity);
        } catch (OfferingNotFoundException | CapacityMismatchException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void finalizeOfferingRecord(String studentId, String offeringCode) throws OfferingRecordNotFoundException {
        OfferingRecordModel offeringRecordModel = new OfferingRecordModel();
        offeringRecordModel.updateStatusOfferingRecord(
                studentId, offeringCode, OfferingRecordEntity.FINALIZED_STATUS
        );
    }

    private void finalizeWaitOfferingRecord(String studentId, String offeringCode) throws OfferingRecordNotFoundException {
        OfferingRecordModel offeringRecordModel = new OfferingRecordModel();
        offeringRecordModel.updateStatusOfferingRecord(
                studentId, offeringCode, OfferingRecordEntity.FINALIZED_WAIT
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
            } else if (offeringRecordStatus.equals(OfferingRecordEntity.NON_FINALIZED_WAIT)) {
                finalizeWaitOfferingRecord(studentId, offeringCode);
            } else if (offeringRecordStatus.equals(OfferingRecordEntity.REMOVED_WAIT)) {
                removeNonFinalizedOffering(studentId, offeringCode);
            }
        }
    }

    public List<Exception> finalizeWeeklySchedule(String studentId) throws
            StudentNotFoundException,
            OfferingRecordNotFoundException,
            OfferingCodeNotInWeeklyScheduleException,
            OfferingNotFoundException,
            SQLException {
        new StudentModel().getStudent(studentId);
        WeeklyScheduleEntity weeklyScheduleEntity;
        weeklyScheduleEntity = WeeklyScheduleRepository.getInstance().getByStudentId(studentId);
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

    public ArrayList<Object> getWeeklySchedulePlan(String studentId) throws
            StudentNotFoundException, OfferingNotFoundException, OfferingRecordNotFoundException {
        ArrayList<Object> data = new ArrayList<>();
        WeeklyScheduleEntity weeklyScheduleEntity = getWeeklySchedule(studentId);
        List<String> weeklyScheduleOfferingCodes = weeklyScheduleEntity.getOfferingCodes();
        for (String offeringCode: weeklyScheduleOfferingCodes) {
            HashMap<String, Object> dataLine = new HashMap<>();
            OfferingEntity offeringEntity = new OfferingModel().getOffering(offeringCode);
            String status = new OfferingRecordModel().getOfferingRecord(studentId, offeringCode).getStatus();
            if (
                    status.equals(OfferingRecordEntity.FINALIZED_STATUS) ||
                            status.equals(OfferingRecordEntity.REMOVED_STATUS)
            ) {

                String classTimeRange = DateParser.getStringFromDates(
                            offeringEntity.getClassTimeStart(),
                            offeringEntity.getClassTimeEnd()
                    );
                dataLine.put("name", offeringEntity.getName());
                dataLine.put("classTime", classTimeRange);
                dataLine.put("weekDays", offeringEntity.getClassTimeDays());
                dataLine.put("type", offeringEntity.getType());

                data.add(dataLine);
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
        return !offeringRecordStatus.equals(OfferingRecordEntity.REMOVED_STATUS) &&
                !offeringRecordStatus.equals(OfferingRecordEntity.REMOVED_WAIT);
    }

    private String getFormattedOfferingStatus(String status) {
        if (
                status.equals(OfferingRecordEntity.FINALIZED_WAIT) ||
                status.equals(OfferingRecordEntity.NON_FINALIZED_WAIT)
        ) {
            return "waiting";
        } else if (
                status.equals(OfferingRecordEntity.FINALIZED_STATUS) ||
                status.equals(OfferingRecordEntity.COMPLETED_STATUS)
        ) {
            return "finalized";
        } else if (
                status.equals(OfferingRecordEntity.NON_FINALIZED_STATUS)
        ) {
            return "non-finalized";
        } else {
            return "other";
        }
    }

    public List<Object> getWeeklyScheduleOfferingEntities(WeeklyScheduleEntity weeklyScheduleEntity)
            throws OfferingNotFoundException, OfferingRecordNotFoundException {
        OfferingModel offeringModel = new OfferingModel();
        OfferingRecordModel offeringRecordModel = new OfferingRecordModel();
        List<Object> offeringEntities = new ArrayList<>();
        for (String offeringCode : weeklyScheduleEntity.getOfferingCodes()) {
            HashMap<String, Object> dataLine = new HashMap<>();
            String offeringRecordStatus =
                    offeringRecordModel.getOfferingRecord(
                            weeklyScheduleEntity.getStudentId(),
                            offeringCode
                    ).getStatus();
            if (isShouldBeShown(offeringRecordStatus)) {
                dataLine.put("offeringData", offeringModel.getOffering(offeringCode));
                dataLine.put("offeringStatus", this.getFormattedOfferingStatus(offeringRecordStatus));
            }
            offeringEntities.add(dataLine);
        }
        return offeringEntities;
    }

    public void resetWeeklySchedule(WeeklyScheduleEntity weeklyScheduleEntity)
            throws OfferingRecordNotFoundException, OfferingCodeNotInWeeklyScheduleException, SQLException {
        OfferingRecordModel offeringRecordModel = new OfferingRecordModel();
        List<String> removeOfferingCodes = new ArrayList<>();
        String studentId = weeklyScheduleEntity.getStudentId();
        for (String offeringCode: weeklyScheduleEntity.getOfferingCodes()) {
            OfferingRecordEntity offeringRecordEntity =
                    offeringRecordModel.getOfferingRecord(studentId, offeringCode);
            if (
                    offeringRecordEntity.getStatus().equals(OfferingRecordEntity.NON_FINALIZED_STATUS) ||
                            offeringRecordEntity.getStatus().equals(OfferingRecordEntity.NON_FINALIZED_WAIT)
            ) {
                removeOfferingCodes.add(offeringCode);
                offeringRecordModel.removeOfferingRecord(studentId, offeringCode);
            } else if (offeringRecordEntity.getStatus().equals(OfferingRecordEntity.COMPLETED_STATUS)) {
                removeOfferingCodes.add(offeringCode);
            } else if (offeringRecordEntity.getStatus().equals(OfferingRecordEntity.REMOVED_STATUS)) {
                offeringRecordModel.updateStatusOfferingRecord(
                        studentId, offeringCode, OfferingRecordEntity.FINALIZED_STATUS);
            } else if (offeringRecordEntity.getStatus().equals(OfferingRecordEntity.REMOVED_WAIT)) {
                offeringRecordModel.updateStatusOfferingRecord(
                        studentId, offeringCode, OfferingRecordEntity.FINALIZED_WAIT);
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

