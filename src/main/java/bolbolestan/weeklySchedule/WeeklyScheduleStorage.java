package bolbolestan.weeklySchedule;

import java.util.ArrayList;
import java.util.List;

public class WeeklyScheduleStorage {
    private static final List<WeeklyScheduleEntity> weeklyScheduleEntities = new ArrayList<>();

    public static void add(WeeklyScheduleEntity weeklyScheduleEntity) {
        weeklyScheduleEntities.add(weeklyScheduleEntity);
    }

    public static void remove(WeeklyScheduleEntity weeklyScheduleEntity) {
        weeklyScheduleEntities.remove(weeklyScheduleEntity);
    }

    public static WeeklyScheduleEntity getByStudentId(String studentId) throws Exception {
        for (WeeklyScheduleEntity entity: weeklyScheduleEntities) {
            if (entity.getStudentId().equals(studentId)) {
                return entity;
            }
        }
        throw new Exception(); //todo proper exception
    }
}
