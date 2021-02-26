package bolbolestan.course;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class CourseEntity {
    private String name;
    private Integer units;
    private Date examTimeStart;
    private Date examTimeEnd;

    private Integer capacity;
    private List<String> prerequisites;

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
}
