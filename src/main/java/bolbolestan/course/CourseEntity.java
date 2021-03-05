package bolbolestan.course;

import java.util.Date;
import java.util.List;

public class CourseEntity {
    public static Integer MINIMUM_UNITS_LIMIT = 12;
    public static Integer MAXIMUM_UNITS_LIMIT = 20;
    private String name;
    private Integer units;
    private Date examTimeStart;
    private Date examTimeEnd;
    private Integer capacity;
    private List<String> prerequisites;
    private String type;

    public CourseEntity() {}

    public CourseEntity(
            String name,
            Integer units,
            Date examTimeStart,
            Date examTimeEnd,
            Integer capacity,
            List<String> prerequisites,
            String type) {
        this.name = name;
        this.units = units;
        this.examTimeStart = examTimeStart;
        this.examTimeEnd = examTimeEnd;
        this.capacity = capacity;
        this.prerequisites = prerequisites;
        this.type = type;
    }

    public CourseEntity(CourseEntity courseEntity) {
        this.name = courseEntity.name;
        this.units = courseEntity.units;
        this.examTimeStart = courseEntity.examTimeStart;
        this.examTimeEnd = courseEntity.examTimeEnd;
        this.capacity = courseEntity.capacity;
        this.prerequisites = courseEntity.prerequisites;
        this.type = courseEntity.type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUnits() {
        return this.units;
    }

    public void setUnits(Integer units) {
        this.units = units;
    }

    public Date getExamTimeEnd() {
        return this.examTimeEnd;
    }

    public void setExamTimeEnd(Date examTimeEnd) {
        this.examTimeEnd = examTimeEnd;
    }

    public Date getExamTimeStart() {
        return examTimeStart;
    }

    public void setExamTimeStart(Date examTimeStart) {
        this.examTimeStart = examTimeStart;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public List<String> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(List<String> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
