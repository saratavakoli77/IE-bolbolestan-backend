package IE.Bolbolestan.student;


import IE.Bolbolestan.bolbolestanExceptions.OfferingNotFoundException;
import IE.Bolbolestan.bolbolestanExceptions.StudentNotFoundException;
import IE.Bolbolestan.offering.OfferingEntity;
import IE.Bolbolestan.offering.OfferingModel;
import IE.Bolbolestan.offeringRecord.OfferingRecordEntity;
import IE.Bolbolestan.offeringRecord.OfferingRecordModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentModel {
    public StudentEntity getStudent(String studentId) throws StudentNotFoundException {
        return StudentStorage.getById(studentId);
    }

    public void addNewStudent(StudentEntity studentEntity) {
        //todo: validate data
        StudentStorage.add(studentEntity);
    }

    public Map<String, Double> getGpaTpu(String studentId) throws OfferingNotFoundException {
        Map<String, Double> unitsGpa = new HashMap<>();
        List<OfferingRecordEntity> offeringRecordEntityList =
                new OfferingRecordModel().getStudentOfferingRecords(studentId);
        double studentGPA = 0.0;
        double unitsSum = 0.0;
        for (OfferingRecordEntity offeringRecordEntity: offeringRecordEntityList) {
            if (offeringRecordEntity.getStatus().equals(OfferingRecordEntity.COMPLETED_STATUS)) {
                OfferingEntity offeringEntity = new OfferingModel().getOffering(offeringRecordEntity.getOfferingCode());
                studentGPA += offeringRecordEntity.getGrade() * offeringEntity.getUnits();
                unitsSum += offeringEntity.getUnits();
            }
        }
        unitsGpa.put("gpa", studentGPA / unitsSum);
        unitsGpa.put("tpu", unitsSum);
        return unitsGpa;
    }

    public List<OfferingRecordEntity> getStudentPassedCourses(String studentId) {
        List<OfferingRecordEntity> offeringRecordEntityList =
                new OfferingRecordModel().getStudentOfferingRecords(studentId);
        List<OfferingRecordEntity> studentPassedCourses = new ArrayList<>();
        for (OfferingRecordEntity offeringRecordEntity: offeringRecordEntityList) {
            if (this.isCoursePassed(offeringRecordEntity)) {
                studentPassedCourses.add(offeringRecordEntity);
            }
        }
        return studentPassedCourses;
    }

    public Boolean hasPassedCourse(String studentId, String offeringCode) {
        List<OfferingRecordEntity> offeringRecordEntityList =
                new OfferingRecordModel().getStudentOfferingRecords(studentId);
        for (OfferingRecordEntity offeringRecordEntity: offeringRecordEntityList) {
            if (this.isCoursePassed(offeringRecordEntity)) {
                if (offeringRecordEntity.getOfferingCode().equals(offeringCode)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Boolean isCoursePassed(OfferingRecordEntity offeringRecordEntity) {
        return offeringRecordEntity.getStatus().equals(OfferingRecordEntity.COMPLETED_STATUS) &&
                offeringRecordEntity.getGrade() >= OfferingRecordEntity.PASSED_GRADE;
    }
}
