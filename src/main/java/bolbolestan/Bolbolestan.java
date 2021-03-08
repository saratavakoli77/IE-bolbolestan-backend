package bolbolestan;


import bolbolestan.offering.OfferingStorage;
import bolbolestan.offeringRecord.OfferingRecordStorage;
import bolbolestan.student.StudentStorage;
import bolbolestan.userInterface.WebServer;

public class Bolbolestan {
    private static WebServer server;
    public static void main(String[] args) throws Exception {
        fillStorages();
        server = new WebServer();
        server.start(8089);
    }

    public static void fillStorages() {
        OfferingStorage.getDataFromApi();
        StudentStorage.getDataFromApi();
        OfferingRecordStorage.getDataFromApi();
    }
}
