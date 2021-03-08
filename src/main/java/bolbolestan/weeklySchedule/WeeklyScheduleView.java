package bolbolestan.weeklySchedule;

import bolbolestan.bolbolestanExceptions.OfferingNotFoundException;
import bolbolestan.bolbolestanExceptions.StudentNotFoundException;
import bolbolestan.student.StudentModel;

import java.util.HashMap;
import java.util.Map;

public class WeeklyScheduleView {
    private WeeklyScheduleModel model;

    public WeeklyScheduleView() {
        model = new WeeklyScheduleModel();
    }

    public HashMap<String, Object> getStudentWeeklySchedule(String studentId) throws
            OfferingNotFoundException, StudentNotFoundException {
        return model.getWeeklySchedulePlan(studentId);
    }
}
