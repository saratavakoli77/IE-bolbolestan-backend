package bolbolestan.mainControllers;

import bolbolestan.bolbolestanExceptions.OfferingNotFoundException;
import bolbolestan.bolbolestanExceptions.StudentNotFoundException;
import bolbolestan.offering.OfferingStorage;
import bolbolestan.offeringRecord.OfferingRecordStorage;
import bolbolestan.student.StudentStorage;
import bolbolestan.weeklySchedule.WeeklyScheduleModel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class InitializerServlet extends HttpServlet {

    public void init() throws ServletException {
        System.out.println("initializing........");
        fillStorages();
        try {
            new WeeklyScheduleModel().addToWeeklySchedule("810196111", "810101001");
        } catch (StudentNotFoundException | OfferingNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void fillStorages() {
        OfferingStorage.getDataFromApi();
        StudentStorage.getDataFromApi();
        OfferingRecordStorage.getDataFromApi();
    }
}