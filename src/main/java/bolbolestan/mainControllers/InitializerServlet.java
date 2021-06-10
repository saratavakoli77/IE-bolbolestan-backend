package bolbolestan.mainControllers;

import bolbolestan.offering.OfferingStorage;
import bolbolestan.offeringRecord.OfferingRecordStorage;
import bolbolestan.student.StudentStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class InitializerServlet extends HttpServlet {

    public void init() throws ServletException {
        System.out.println("initializing........");
        fillStorages();
    }

    public static void fillStorages() {
        OfferingStorage.getDataFromApi();
        StudentStorage.getDataFromApi();
        OfferingRecordStorage.getDataFromApi();
    }
}