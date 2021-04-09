package bolbolestan.waitList;

import bolbolestan.bolbolestanExceptions.CapacityMismatchException;
import bolbolestan.bolbolestanExceptions.OfferingNotFoundException;
import bolbolestan.offeringRecord.OfferingRecordModel;

public class CheckListJob implements Runnable {

    @Override
    public void run() {
        try {
            new OfferingRecordModel().finalizeWaitList();
            System.out.println("job is running!");
        } catch (OfferingNotFoundException | CapacityMismatchException e) {
            e.printStackTrace();
        }
    }
}