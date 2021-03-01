package bolbolestan.requestHandler;

import bolbolestan.bolbolestanExceptions.*;
import bolbolestan.course.CourseEntity;
import bolbolestan.course.DaysOfWeek;
import bolbolestan.offering.OfferingEntity;
import bolbolestan.offering.OfferingModel;
import bolbolestan.student.StudentEntity;
import bolbolestan.student.StudentModel;
import bolbolestan.tools.DateParser;
import bolbolestan.weeklySchedule.WeeklyScheduleEntity;
import bolbolestan.weeklySchedule.WeeklyScheduleModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RequestHandlerTest {
    @Before
    public void setUp() throws ParseException, OfferingNotFoundException, StudentNotFoundException {
        StudentEntity testStudent = new StudentEntity(
                "810196000", "testStudent", "1396"
        );
        new StudentModel().addNewStudent(testStudent);

        CourseEntity courseEntity1 = new CourseEntity(
                "course1",
                4,
                DateParser.getDateFromExamFormat("2021-10-01T08:00:00"),
                DateParser.getDateFromExamFormat("2021-10-01T10:30:00"),
                1,
                null
        );

        CourseEntity courseEntity2 = new CourseEntity(
                "course2",
                5,
                DateParser.getDateFromExamFormat("2021-10-02T08:00:00"),
                DateParser.getDateFromExamFormat("2021-10-02T10:30:00"),
                50,
                null
        );

        CourseEntity courseEntity3 = new CourseEntity(
                "course3",
                6,
                DateParser.getDateFromExamFormat("2021-10-03T08:00:00"),
                DateParser.getDateFromExamFormat("2021-10-03T10:30:00"),
                6,
                null
        );

        CourseEntity courseEntity4 = new CourseEntity(
                "course4",
                6,
                DateParser.getDateFromExamFormat("2021-10-03T09:00:00"),
                DateParser.getDateFromExamFormat("2021-10-03T10:30:00"),
                5,
                null
        );

        OfferingEntity testOffering1 = new OfferingEntity(
                courseEntity1,
                "810112301",
                "testInstructor",
                Arrays.asList(DaysOfWeek.Saturday, DaysOfWeek.Monday),
                DateParser.getDatesFromString("14-15:30").get("start"),
                DateParser.getDatesFromString("14-15:30").get("end"),
                0
        );

        OfferingEntity testOffering2 = new OfferingEntity(
                courseEntity2,
                "810111101",
                "testInstructor",
                Arrays.asList(DaysOfWeek.Sunday, DaysOfWeek.Tuesday),
                DateParser.getDatesFromString("14-15:30").get("start"),
                DateParser.getDatesFromString("14-15:30").get("end"),
                0
        );

        OfferingEntity testOffering3 = new OfferingEntity(
                courseEntity3,
                "810110001",
                "testInstructor",
                Arrays.asList(DaysOfWeek.Sunday, DaysOfWeek.Tuesday),
                DateParser.getDatesFromString("9-10:30").get("start"),
                DateParser.getDatesFromString("9-10:30").get("end"),
                0
        );

        OfferingEntity testOffering4 = new OfferingEntity(
                courseEntity4,
                "810110005",
                "testInstructor",
                Arrays.asList(DaysOfWeek.Sunday, DaysOfWeek.Tuesday),
                DateParser.getDatesFromString("15-16").get("start"),
                DateParser.getDatesFromString("15-16").get("end"),
                0
        );


        OfferingModel offeringModel = new OfferingModel();
        offeringModel.addNewOffering(testOffering1);
        offeringModel.addNewOffering(testOffering2);
        offeringModel.addNewOffering(testOffering3);
        offeringModel.addNewOffering(testOffering4);

        WeeklyScheduleModel weeklyScheduleModel = new WeeklyScheduleModel();
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810112301");
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810111101");
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810110001");

    }

    @Test
    public void showWeeklyScheduleNonFinalize() {
        RequestHandler requestHandler = new RequestHandler();
        HashMap<String, Object> response = requestHandler.getWeeklySchedule("810196000");

        Assert.assertTrue((Boolean) response.get("success"));
        Assert.assertEquals(((WeeklyScheduleEntity) response.get("data")).getStudentId(), "810196000");
        Assert.assertTrue(((WeeklyScheduleEntity) response.get("data")).getOfferingCodes().contains("810112301"));
        Assert.assertTrue(((WeeklyScheduleEntity) response.get("data")).getOfferingCodes().contains("810111101"));
        Assert.assertTrue(((WeeklyScheduleEntity) response.get("data")).getOfferingCodes().contains("810110001"));
        Assert.assertEquals(((WeeklyScheduleEntity) response.get("data")).getStatus(), WeeklyScheduleEntity.NON_FINALIZED_STATUS);
    }

    @Test
    public void showWeeklyScheduleFinalize() {
        RequestHandler requestHandler = new RequestHandler();
        HashMap<String, Object> finalizeResponse = requestHandler.finalizeSchedule("810196000");
        Assert.assertTrue((Boolean) finalizeResponse.get("success"));
        HashMap<String, Object> getWeeklyScheduleResponse = requestHandler.getWeeklySchedule("810196000");
        Assert.assertTrue((Boolean) getWeeklyScheduleResponse.get("success"));
        Assert.assertEquals(((WeeklyScheduleEntity) getWeeklyScheduleResponse.get("data")).getStudentId(), "810196000");
        Assert.assertTrue(((WeeklyScheduleEntity) getWeeklyScheduleResponse.get("data")).getOfferingCodes().contains("810112301"));
        Assert.assertTrue(((WeeklyScheduleEntity) getWeeklyScheduleResponse.get("data")).getOfferingCodes().contains("810111101"));
        Assert.assertTrue(((WeeklyScheduleEntity) getWeeklyScheduleResponse.get("data")).getOfferingCodes().contains("810110001"));
        Assert.assertEquals(((WeeklyScheduleEntity) getWeeklyScheduleResponse.get("data")).getStatus(), WeeklyScheduleEntity.FINALIZED_STATUS);
    }

    @Test
    public void showWeeklyScheduleFinalizeFail() throws OfferingNotFoundException, StudentNotFoundException {
        RequestHandler requestHandler = new RequestHandler();
        WeeklyScheduleModel weeklyScheduleModel = new WeeklyScheduleModel();
        weeklyScheduleModel.addToWeeklySchedule("810196000", "810110005");
        OfferingModel offeringModel = new OfferingModel();
        OfferingEntity offeringEntity = offeringModel.getOffering("810112301");
        offeringEntity.setRegistered(offeringEntity.getCapacity());
        HashMap<String, Object> finalizeResponse = requestHandler.finalizeSchedule("810196000");
        List<String> exceptionMessageList = (List<String>) finalizeResponse.get("error");
        Assert.assertFalse((Boolean) finalizeResponse.get("success"));
        Assert.assertFalse(exceptionMessageList.isEmpty());
        Assert.assertEquals(exceptionMessageList.size(), 4);
        Assert.assertTrue(exceptionMessageList.contains(new MaximumUnitsException().getMessage()));
        Assert.assertTrue(exceptionMessageList.contains(new CapacityException("810112301").getMessage()));
        Assert.assertTrue(
                exceptionMessageList.contains(new ClassCollisionException("810111101", "810110005").getMessage())
        );
        Assert.assertTrue(
                exceptionMessageList.contains(new ExamTimeCollisionException("810110001", "810110005").getMessage())
        );

    }

}
