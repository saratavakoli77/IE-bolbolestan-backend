package IE.Bolbolestan.waitList;


import IE.Bolbolestan.bolbolestanExceptions.CapacityMismatchException;
import IE.Bolbolestan.bolbolestanExceptions.OfferingNotFoundException;
import IE.Bolbolestan.offeringRecord.OfferingRecordModel;

import java.sql.SQLException;

public class CheckListJob implements Runnable {

    @Override
    public void run() {
        try {
            new OfferingRecordModel().finalizeWaitList();
            System.out.println("job is running!");
        } catch (OfferingNotFoundException | CapacityMismatchException | SQLException e) {
            e.printStackTrace();
        }
    }
}
