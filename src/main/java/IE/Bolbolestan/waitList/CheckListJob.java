package IE.Bolbolestan.waitList;


import IE.Bolbolestan.bolbolestanExceptions.CapacityMismatchException;
import IE.Bolbolestan.bolbolestanExceptions.OfferingNotFoundException;
import IE.Bolbolestan.offeringRecord.OfferingRecordModel;

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
