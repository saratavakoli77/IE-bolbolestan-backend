package bolbolestan;


import bolbolestan.offering.OfferingStorage;
import bolbolestan.offeringRecord.OfferingRecordStorage;
import bolbolestan.student.StudentStorage;
import bolbolestan.userInterface.TerminalServer;

public class Bolbolestan {
    public static void main(String[] args) {
        fillStorages();
        TerminalServer terminalServer = new TerminalServer();
        terminalServer.listen();
    }

    public static void fillStorages() {
        OfferingStorage.getDataFromApi();
        StudentStorage.getDataFromApi();
        OfferingRecordStorage.getDataFromApi();
    }
}
