package bolbolestan.offering;

import bolbolestan.course.CourseEntity;
import bolbolestan.course.DaysOfWeek;

import java.util.Date;
import java.util.List;

public class OfferingEntity extends CourseEntity {
    private String code;
    private String instructor;
    private List<DaysOfWeek> classTimeDays;
//    private String classTimeHour;
    private Date startHour;
    private Date endHour;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

//    public void setStartHour
}
