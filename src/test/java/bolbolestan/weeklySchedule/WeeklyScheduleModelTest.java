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
import java.util.*;

public class WeeklyScheduleModelTest {
    @Before
    public void setUp() throws ParseException {
        StudentEntity testStudent = new StudentEntity(
                "810196000", "testStudent", "test","1396"
        );
        new StudentModel().addNewStudent(testStudent);

        CourseEntity courseEntity1 = new CourseEntity(
                "8101000",
                "course1",
                4,
                DateParser.getDateFromExamFormat("2021-10-01T08:00:00"),
                DateParser.getDateFromExamFormat("2021-10-01T10:30:00"),
                60,
                null,
                "Asli"
        );

        CourseEntity courseEntity2 = new CourseEntity(
                "8101001",
                "course2",
                4,
                DateParser.getDateFromExamFormat("2021-10-02T08:00:00"),
                DateParser.getDateFromExamFormat("2021-10-02T10:30:00"),
                60,
                null,
                "Asli"
        );

        CourseEntity courseEntity3 = new CourseEntity(
                "8101002",
                "course3",
                8,
                DateParser.getDateFromExamFormat("2021-10-03T08:00:00"),
                DateParser.getDateFromExamFormat("2021-10-03T10:30:00"),
                60,
                null,
                "Asli"
        );

        CourseEntity courseEntity4 = new CourseEntity(
                "8101003",
                "course4",
                20,
                DateParser.getDateFromExamFormat("2021-10-04T08:00:00"),
                DateParser.getDateFromExamFormat("2021-10-04T10:30:00"),
                60,
                null,
                "Asli"
        );

        OfferingEntity testOffering1 = new OfferingEntity(
                courseEntity1,
                "testInstructor",
                Arrays.asList(DaysOfWeek.Saturday, DaysOfWeek.Monday),
                DateParser.getDatesFromString("14-15:30").get("start"),
                DateParser.getDatesFromString("14-15:30").get("end"),
                0,
                "01"
        );

        OfferingEntity testOffering2 = new OfferingEntity(
                courseEntity2,
                "testInstructor",
                Arrays.asList(DaysOfWeek.Sunday, DaysOfWeek.Tuesday),
                DateParser.getDatesFromString("14-15:30").get("start"),
                DateParser.getDatesFromString("14-15:30").get("end"),
                0,
                "01"
        );

        OfferingEntity testOffering3 = new OfferingEntity(
                courseEntity3,
                "testInstructor",
                Arrays.asList(DaysOfWeek.Saturday, DaysOfWeek.Monday),
                DateParser.getDatesFromString("14-15:30").get("start"),
                DateParser.getDatesFromString("14-15:30").get("end"),
                0,
                "01"
        );

        OfferingEntity testOffering4 = new OfferingEntity(
                courseEntity4,
                "testInstructor",
                Collections.singletonList(DaysOfWeek.Wednesday),
                DateParser.getDatesFromString("14-15:30").get("start"),
                DateParser.getDatesFromString("14-15:30").get("end"),
                0,
                "01"
        );

        OfferingModel offeringModel = new OfferingModel();
        offeringModel.addNewOffering(testOffering1);
        offeringModel.addNewOffering(testOffering2);
        offeringModel.addNewOffering(testOffering3);
        offeringModel.addNewOffering(testOffering4);
    }

    @Test(expected = StudentNotFoundException.class)
    public void finalizeStudentNotExistTest() throws StudentNotFoundException, OfferingRecordNotFoundException, OfferingCodeNotInWeeklyScheduleException {
        new WeeklyScheduleModel().finalizeWeeklySchedule("8101911111");
    }

    @Test(expected = StudentNotFoundException.class)
    public void addToScheduleStudentNotExistTest() throws StudentNotFoundException, OfferingRecordNotFoundException, OfferingCodeNotInWeeklyScheduleException {
        new WeeklyScheduleModel().finalizeWeeklySchedule("8101911111");
    }

    @Test
    public void finalizeUnitsUnderLimitTest() throws StudentNotFoundException, OfferingNotFoundException, OfferingRecordNotFoundException, OfferingCodeNotInWeeklyScheduleException {
        WeeklyScheduleModel weeklyScheduleModel = new WeeklyScheduleModel();
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810100101");
        List<Exception> exceptionList = weeklyScheduleModel.finalizeWeeklySchedule("810196000");
        Assert.assertEquals(exceptionList.get(0).getMessage(), new MinimumUnitsException().getMessage());
    }

    @Test
    public void finalizeUnitsExceedLimitTest() throws StudentNotFoundException, OfferingNotFoundException, OfferingRecordNotFoundException, OfferingCodeNotInWeeklyScheduleException {
        WeeklyScheduleModel weeklyScheduleModel = new WeeklyScheduleModel();
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810100301");
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810100101");

        List<Exception> exceptionList = weeklyScheduleModel.finalizeWeeklySchedule("810196000");
        Assert.assertEquals(exceptionList.get(0).getMessage(), new MaximumUnitsException().getMessage());
    }

    @Test
    public void addToSchedule2CoursesSessionColliedTest() throws StudentNotFoundException, OfferingNotFoundException, OfferingRecordNotFoundException {
        WeeklyScheduleModel weeklyScheduleModel = new WeeklyScheduleModel();
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810100001");
        List<Exception> exceptionList = weeklyScheduleModel.addToWeeklySchedule("810196000", "810100201");

        Assert.assertEquals(
                exceptionList.get(0).getMessage(), new ClassCollisionException("810100001", "810100201").getMessage()
        );
    }


    @Test
    public void finalizeWeeklyScheduleNoInitialized() throws StudentNotFoundException, OfferingRecordNotFoundException, OfferingCodeNotInWeeklyScheduleException {
        WeeklyScheduleModel weeklyScheduleModel = new WeeklyScheduleModel();
        List<Exception> exceptionList = weeklyScheduleModel.finalizeWeeklySchedule("810196000");
        Assert.assertEquals(
                exceptionList.get(0).getMessage(), new MinimumUnitsException().getMessage()
        );
    }

    @Test
    public void finalizeWeeklySchedule() throws StudentNotFoundException, OfferingNotFoundException, OfferingRecordNotFoundException, OfferingCodeNotInWeeklyScheduleException {
        WeeklyScheduleModel weeklyScheduleModel = new WeeklyScheduleModel();
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810100101");
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810100201");

        OfferingModel offeringModel = new OfferingModel();
        OfferingEntity offeringEntity = offeringModel.getOffering("810100201");
        int registeredBeforeFinalize = offeringEntity.getRegistered();

        List<Exception> exceptionList = weeklyScheduleModel.finalizeWeeklySchedule("810196000");
        WeeklyScheduleEntity weeklyScheduleEntity = weeklyScheduleModel.getWeeklySchedule("810196000");

        Assert.assertTrue(exceptionList.isEmpty());
        Assert.assertNotNull(weeklyScheduleEntity);
//        Assert.assertEquals(weeklyScheduleEntity.getStatus(), WeeklyScheduleEntity.FINALIZED_STATUS);
        Assert.assertEquals(weeklyScheduleEntity.getStudentId(), "810196000");
        List<String> offeringCodes = weeklyScheduleEntity.getOfferingCodes();
        Assert.assertTrue(offeringCodes.contains("810100101"));
        Assert.assertTrue(offeringCodes.contains("810100201"));
        Assert.assertEquals(registeredBeforeFinalize + 1, offeringEntity.getRegistered().longValue());
    }


    @After
    public void cleanUp() {
        StudentStorage.removeAll();
        WeeklyScheduleStorage.removeAll();
        OfferingStorage.removeAll();
    }
}
