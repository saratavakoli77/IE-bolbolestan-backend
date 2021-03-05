package bolbolestan.weeklySchedule;

import bolbolestan.bolbolestanExceptions.*;
import bolbolestan.course.CourseEntity;
import bolbolestan.course.DaysOfWeek;
import bolbolestan.offering.OfferingEntity;
import bolbolestan.offering.OfferingModel;
import bolbolestan.offering.OfferingStorage;
import bolbolestan.student.StudentEntity;
import bolbolestan.student.StudentModel;
import bolbolestan.student.StudentStorage;
import bolbolestan.tools.DateParser;
import org.junit.*;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

public class WeeklyScheduleModelTest {
    @Before
    public void setUp() throws ParseException {
        StudentEntity testStudent = new StudentEntity(
                "810196000", "testStudent", "test","1396"
        );
        new StudentModel().addNewStudent(testStudent);

        CourseEntity courseEntity1 = new CourseEntity(
                "course1",
                4,
                DateParser.getDateFromExamFormat("2021-10-01T08:00:00"),
                DateParser.getDateFromExamFormat("2021-10-01T10:30:00"),
                60,
                null,
                "Asli"
        );

        CourseEntity courseEntity2 = new CourseEntity(
                "course2",
                5,
                DateParser.getDateFromExamFormat("2021-10-02T08:00:00"),
                DateParser.getDateFromExamFormat("2021-10-02T10:30:00"),
                50,
                null,
                "Asli"
        );

        CourseEntity courseEntity3 = new CourseEntity(
                "course3",
                6,
                DateParser.getDateFromExamFormat("2021-10-03T08:00:00"),
                DateParser.getDateFromExamFormat("2021-10-03T10:30:00"),
                1,
                null,
                "Asli"
        );

        CourseEntity courseEntity4 = new CourseEntity(
                "course4",
                6,
                DateParser.getDateFromExamFormat("2021-10-04T08:00:00"),
                DateParser.getDateFromExamFormat("2021-10-04T10:30:00"),
                10,
                null,
                "Asli"
        );

        CourseEntity courseEntity5 = new CourseEntity(
                "course5",
                8,
                DateParser.getDateFromExamFormat("2021-10-01T08:00:00"),
                DateParser.getDateFromExamFormat("2021-10-01T10:30:00"),
                55,
                null,
                "Asli"
        );

        CourseEntity courseEntity6 = new CourseEntity(
                "course6",
                8,
                DateParser.getDateFromExamFormat("2021-10-01T08:00:00"),
                DateParser.getDateFromExamFormat("2021-10-01T10:30:00"),
                55,
                null,
                "Asli"
        );

        OfferingEntity testOffering1 = new OfferingEntity(
                courseEntity1,
                "810112301",
                "testInstructor",
                Arrays.asList(DaysOfWeek.Saturday, DaysOfWeek.Monday),
                DateParser.getDatesFromString("14-15:30").get("start"),
                DateParser.getDatesFromString("14-15:30").get("end"),
                0,
                "01"
        );

        OfferingEntity testOffering2 = new OfferingEntity(
                courseEntity1,
                "810112302",
                "testInstructor",
                Arrays.asList(DaysOfWeek.Sunday, DaysOfWeek.Tuesday),
                DateParser.getDatesFromString("14-15:30").get("start"),
                DateParser.getDatesFromString("14-15:30").get("end"),
                0,
                "01"
        );

        OfferingEntity testOffering3 = new OfferingEntity(
                courseEntity2,
                "810111101",
                "testInstructor",
                Arrays.asList(DaysOfWeek.Sunday, DaysOfWeek.Tuesday),
                DateParser.getDatesFromString("14-15:30").get("start"),
                DateParser.getDatesFromString("14-15:30").get("end"),
                0,
                "01"
        );

        OfferingEntity testOffering4 = new OfferingEntity(
                courseEntity3,
                "810110001",
                "testInstructor",
                Arrays.asList(DaysOfWeek.Sunday, DaysOfWeek.Tuesday),
                DateParser.getDatesFromString("9-10:30").get("start"),
                DateParser.getDatesFromString("9-10:30").get("end"),
                0,
                "01"
        );

        OfferingEntity testOffering5 = new OfferingEntity(
                courseEntity4,
                "810120001",
                "testInstructor",
                Arrays.asList(DaysOfWeek.Saturday, DaysOfWeek.Monday),
                DateParser.getDatesFromString("9-10:30").get("start"),
                DateParser.getDatesFromString("9-10:30").get("end"),
                0,
                "01"
        );

        OfferingEntity testOffering6 = new OfferingEntity(
                courseEntity2,
                "810111102",
                "testInstructor",
                Arrays.asList(DaysOfWeek.Saturday, DaysOfWeek.Monday),
                DateParser.getDatesFromString("14-15:30").get("start"),
                DateParser.getDatesFromString("14-15:30").get("end"),
                0,
                "01"
        );

        OfferingEntity testOffering7 = new OfferingEntity(
                courseEntity3,
                "810110002",
                "testInstructor",
                Arrays.asList(DaysOfWeek.Saturday, DaysOfWeek.Monday),
                DateParser.getDatesFromString("14-15:30").get("start"),
                DateParser.getDatesFromString("14-15:30").get("end"),
                0,
                "01"
        );

        OfferingEntity testOffering8 = new OfferingEntity(
                courseEntity5,
                "810000000",
                "testInstructor",
                Arrays.asList(DaysOfWeek.Sunday, DaysOfWeek.Tuesday),
                DateParser.getDatesFromString("14-15:30").get("start"),
                DateParser.getDatesFromString("14-15:30").get("end"),
                0,
                "01"
        );

        OfferingEntity testOffering9 = new OfferingEntity(
                courseEntity6,
                "810000100",
                "testInstructor",
                Arrays.asList(DaysOfWeek.Sunday, DaysOfWeek.Tuesday),
                DateParser.getDatesFromString("9-10:30").get("start"),
                DateParser.getDatesFromString("9-10:30").get("end"),
                0,
                "01"
        );

        OfferingModel offeringModel = new OfferingModel();
        offeringModel.addNewOffering(testOffering1);
        offeringModel.addNewOffering(testOffering2);
        offeringModel.addNewOffering(testOffering3);
        offeringModel.addNewOffering(testOffering4);
        offeringModel.addNewOffering(testOffering5);
        offeringModel.addNewOffering(testOffering6);
        offeringModel.addNewOffering(testOffering7);
        offeringModel.addNewOffering(testOffering8);
        offeringModel.addNewOffering(testOffering9);
    }

    @Test(expected = StudentNotFoundException.class)
    public void finalizeStudentNotExistTest() throws StudentNotFoundException {
        new WeeklyScheduleModel().finalizeWeeklySchedule("8101911111");
    }

    @Test
    public void finalizeUnitsUnderLimitTest() throws StudentNotFoundException, OfferingNotFoundException {
        WeeklyScheduleModel weeklyScheduleModel = new WeeklyScheduleModel();
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810112301");
        List<Exception> exceptionList = weeklyScheduleModel.finalizeWeeklySchedule("810196000");
        Assert.assertEquals(exceptionList.get(0).getMessage(), new MinimumUnitsException().getMessage());
    }

    @Test
    public void finalizeUnitsExceedLimitTest() throws StudentNotFoundException, OfferingNotFoundException {
        WeeklyScheduleModel weeklyScheduleModel = new WeeklyScheduleModel();
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810112301");
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810111101");
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810110001");
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810120001");

        List<Exception> exceptionList = weeklyScheduleModel.finalizeWeeklySchedule("810196000");
        Assert.assertEquals(exceptionList.get(0).getMessage(), new MaximumUnitsException().getMessage());
    }

    @Test
    public void finalize2CoursesSessionColliedTest() throws StudentNotFoundException, OfferingNotFoundException {
        WeeklyScheduleModel weeklyScheduleModel = new WeeklyScheduleModel();
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810112301");
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810111102");
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810110001");

        List<Exception> exceptionList = weeklyScheduleModel.finalizeWeeklySchedule("810196000");
        Assert.assertEquals(
                exceptionList.get(0).getMessage(), new ClassCollisionException("810112301", "810111102").getMessage()
        );
    }

    @Test
    public void finalize3CoursesSessionColliedTest() throws StudentNotFoundException, OfferingNotFoundException {
        WeeklyScheduleModel weeklyScheduleModel = new WeeklyScheduleModel();
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810112301");
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810111102");
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810110002");

        List<Exception> exceptionList = weeklyScheduleModel.finalizeWeeklySchedule("810196000");
        Assert.assertEquals(
                exceptionList.get(0).getMessage(), new ClassCollisionException("810112301", "810111102").getMessage()
        );
        Assert.assertEquals(
                exceptionList.get(1).getMessage(), new ClassCollisionException("810112301", "810110002").getMessage()
        );
        Assert.assertEquals(
                exceptionList.get(2).getMessage(), new ClassCollisionException("810111102", "810110002").getMessage()
        );
    }

    @Test
    public void finalize2CoursesExamsColliedTest() throws StudentNotFoundException, OfferingNotFoundException {
        WeeklyScheduleModel weeklyScheduleModel = new WeeklyScheduleModel();
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810000000");
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810112301");

        List<Exception> exceptionList = weeklyScheduleModel.finalizeWeeklySchedule("810196000");
        Assert.assertEquals(
                exceptionList.get(0).getMessage(), new ExamTimeCollisionException("810000000", "810112301").getMessage()
        );
    }

    @Test
    public void finalize3CoursesExamsColliedTest() throws StudentNotFoundException, OfferingNotFoundException {
        WeeklyScheduleModel weeklyScheduleModel = new WeeklyScheduleModel();
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810000000");
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810112301");
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810000100");

        List<Exception> exceptionList = weeklyScheduleModel.finalizeWeeklySchedule("810196000");
        Assert.assertEquals(
                exceptionList.get(0).getMessage(), new ExamTimeCollisionException("810000000", "810112301").getMessage()
        );
        Assert.assertEquals(
                exceptionList.get(1).getMessage(), new ExamTimeCollisionException("810000000", "810000100").getMessage()
        );
        Assert.assertEquals(
                exceptionList.get(2).getMessage(), new ExamTimeCollisionException("810112301", "810000100").getMessage()
        );
    }

    @Test
    public void finalizeNotEnoughCapacity() throws StudentNotFoundException, OfferingNotFoundException {
        OfferingEntity offeringTest = new OfferingModel().getOffering("810110001");
        offeringTest.setRegistered(1);
        offeringTest.setUnits(13);
        WeeklyScheduleModel weeklyScheduleModel = new WeeklyScheduleModel();
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810110001");
        List<Exception> exceptionList = weeklyScheduleModel.finalizeWeeklySchedule("810196000");
        Assert.assertEquals(
                exceptionList.get(0).getMessage(), new CapacityException("810110001").getMessage()
        );
    }

    @Test
    public void finalizeWeeklyScheduleNoInitialized() throws StudentNotFoundException {
        WeeklyScheduleModel weeklyScheduleModel = new WeeklyScheduleModel();
        List<Exception> exceptionList = weeklyScheduleModel.finalizeWeeklySchedule("810196000");
        Assert.assertEquals(
                exceptionList.get(0).getMessage(), new MinimumUnitsException().getMessage()
        );
    }

    @Test
    public void finalizeWeeklySchedule() throws StudentNotFoundException, OfferingNotFoundException {
        WeeklyScheduleModel weeklyScheduleModel = new WeeklyScheduleModel();
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810112301");
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810111101");
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810110001");

        OfferingModel offeringModel = new OfferingModel();
        OfferingEntity offeringEntity = offeringModel.getOffering("810112301");
        int registeredBeforeFinalize = offeringEntity.getRegistered();

        List<Exception> exceptionList = weeklyScheduleModel.finalizeWeeklySchedule("810196000");
        WeeklyScheduleEntity weeklyScheduleEntity = weeklyScheduleModel.getWeeklySchedule("810196000");

        Assert.assertTrue(exceptionList.isEmpty());
        Assert.assertNotNull(weeklyScheduleEntity);
        Assert.assertEquals(weeklyScheduleEntity.getStatus(), WeeklyScheduleEntity.FINALIZED_STATUS);
        Assert.assertEquals(weeklyScheduleEntity.getStudentId(), "810196000");
        List<String> offeringCodes = weeklyScheduleEntity.getOfferingCodes();
        Assert.assertTrue(offeringCodes.contains("810112301"));
        Assert.assertTrue(offeringCodes.contains("810111101"));
        Assert.assertTrue(offeringCodes.contains("810110001"));
        Assert.assertEquals(registeredBeforeFinalize + 1, offeringEntity.getRegistered().longValue());
    }

    @Test(expected = StudentNotFoundException.class)
    public void getWeeklyScheduleStudentNotFoundTest() throws StudentNotFoundException {
        WeeklyScheduleModel weeklyScheduleModel = new WeeklyScheduleModel();
        weeklyScheduleModel.getWeeklySchedule("810196001");
    }

    @Test
    public void getWeeklyScheduleTest() throws OfferingNotFoundException, StudentNotFoundException {
        WeeklyScheduleModel weeklyScheduleModel = new WeeklyScheduleModel();
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810112301");

        WeeklyScheduleEntity weeklyScheduleEntity = weeklyScheduleModel.getWeeklySchedule("810196000");

        Assert.assertNotNull(weeklyScheduleEntity);
        Assert.assertEquals(weeklyScheduleEntity.getStatus(), WeeklyScheduleEntity.NON_FINALIZED_STATUS);
        Assert.assertEquals(weeklyScheduleEntity.getStudentId(), "810196000");
        List<String> offeringCodes = weeklyScheduleEntity.getOfferingCodes();
        Assert.assertTrue(offeringCodes.contains("810112301"));

        weeklyScheduleModel.addToWeeklySchedule("810196000", "810111101");
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810110001");

        List<Exception> exceptionList = weeklyScheduleModel.finalizeWeeklySchedule("810196000");
        Assert.assertTrue(exceptionList.isEmpty());
        Assert.assertEquals(weeklyScheduleEntity.getStatus(), WeeklyScheduleEntity.FINALIZED_STATUS);
        Assert.assertTrue(offeringCodes.contains("810111101"));
        Assert.assertTrue(offeringCodes.contains("810110001"));
    }

    @After
    public void cleanUp() {
        StudentStorage.removeAll();
        WeeklyScheduleStorage.removeAll();
        OfferingStorage.removeAll();
    }
}
