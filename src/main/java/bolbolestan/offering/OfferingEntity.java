package bolbolestan.offering;

import bolbolestan.course.CourseEntity;
import bolbolestan.course.DaysOfWeek;

import java.util.Date;
import java.util.List;

public class OfferingEntity extends CourseEntity {
    private String instructor;
    private List<DaysOfWeek> classTimeDays;
    private Date classTimeStart;
    private Date classTimeEnd;
    private String classCode;
    private Integer registered = 0;

    public OfferingEntity() {}

    public OfferingEntity(
            CourseEntity courseEntity,
            String instructor,
            List<DaysOfWeek> classTimeDays,
            Date classTimeStart,
            Date classTimeEnd,
            Integer registered,
            String classCode) {
        super(courseEntity);
        this.instructor = instructor;
        this.classTimeDays = classTimeDays;
        this.classTimeStart = classTimeStart;
        this.classTimeEnd = classTimeEnd;
        this.classCode = classCode;
        this.registered = registered;
    }

    public String getOfferingCode() {
        return this.getCode() + this.classCode;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public List<DaysOfWeek> getClassTimeDays() {
        return classTimeDays;
    }

    public void setClassTimeDays(List<DaysOfWeek> classTimeDays) {
        this.classTimeDays = classTimeDays;
    }

    public void setClassTimeStart(Date classTimeStart) {
        this.classTimeStart = classTimeStart;
    }

    public Date getClassTimeStart() {
        return classTimeStart;
    }

    public void setClassTimeEnd(Date classTimeEnd) {
        this.classTimeEnd = classTimeEnd;
    }

    public Date getClassTimeEnd() {
        return classTimeEnd;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setRegistered(Integer registered) {
        this.registered = registered;
    }

    public Integer getRegistered() {
        return registered;
    }
}
