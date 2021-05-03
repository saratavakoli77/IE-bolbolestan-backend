package IE.Bolbolestan.offering;


import IE.Bolbolestan.bolbolestanExceptions.OfferingNotFoundException;
import IE.Bolbolestan.course.CourseEntity;
import IE.Bolbolestan.course.DaysOfWeek;
import IE.Bolbolestan.tools.DateParser;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class OfferingEntity extends CourseEntity {
    private String instructor;
    private String stringClassTimeDays;
    private Date classTimeStart;
    private Date classTimeEnd;
    private String classCode;
    private Integer registered = 0;

    public OfferingEntity() {}

    public OfferingEntity(
            CourseEntity courseEntity,
            String instructor,
            String stringClassTimeDays,
            Date classTimeStart,
            Date classTimeEnd,
            String classCode,
            Integer registered) {
        super(courseEntity);
        this.instructor = instructor;
        this.stringClassTimeDays = stringClassTimeDays;
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
        List<String> classDays = Arrays.asList(this.stringClassTimeDays.split(", "));
        List<DaysOfWeek> classDaysEnum = new ArrayList<>();
        for (String classDay: classDays) {
            classDaysEnum.add(Enum.valueOf(DaysOfWeek.class, classDay));
        }
        return classDaysEnum;
    }

    public void setClassTimeDays(List<DaysOfWeek> classTimeDays) {
        String classTimeString = "";
        for (DaysOfWeek daysOfWeek: classTimeDays) {
            classTimeString += daysOfWeek.toString();
            classTimeString += ", ";
        }
        StringBuilder stringBuilder = new StringBuilder(classTimeString);
        stringBuilder.deleteCharAt(classTimeString.length() - 1);
        stringBuilder.deleteCharAt(classTimeString.length() - 2);
        classTimeString = stringBuilder.toString();
        this.setStringClassTimeDays(classTimeString);
    }

    public void setStringClassTimeDays(String stringClassTimeDays) { this.stringClassTimeDays = stringClassTimeDays;}

    public String getStringClassTimeDays() { return this.stringClassTimeDays; }

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
        if (classCode.length() == 1) {
            this.classCode = "0" + classCode;
        } else {
            this.classCode = classCode;
        }
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

    public String getFormattedClassDate() {
        return DateParser.getStringFromDates(
                this.getClassTimeStart(),
                this.getClassTimeEnd()
        );
    }

    public List<String> getPrerequisiteNames() {
        List<String> prerequisiteNames = new ArrayList<>();
        for (String code: this.getPrerequisites()) {
            try {
                prerequisiteNames.add(OfferingRepository.getInstance().getById(Arrays.asList(code, this.classCode)).getName());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return prerequisiteNames;
    }


}
