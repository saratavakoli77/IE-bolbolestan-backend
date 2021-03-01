package bolbolestan.weeklySchedule;

import bolbolestan.bolbolestanExceptions.WeeklyScheduleDoesNotExistException;

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

    public static WeeklyScheduleEntity getByStudentId(String studentId) throws WeeklyScheduleDoesNotExistException {
        for (WeeklyScheduleEntity entity: weeklyScheduleEntities) {
            if (entity.getStudentId().equals(studentId)) {
                return entity;
            }
        }
        throw new WeeklyScheduleDoesNotExistException();
    }

    public static void removeAll() {
        weeklyScheduleEntities.clear();
    }
}
