package bolbolestan.offering;

import bolbolestan.bolbolestanExceptions.CapacityMismatchException;
import bolbolestan.bolbolestanExceptions.OfferingNotFoundException;

public class OfferingModel {
    public void addNewOffering(OfferingEntity offeringEntity) {
        //todo: validate data
        OfferingStorage.add(offeringEntity);
    }

    public OfferingEntity getOffering(String code) throws OfferingNotFoundException {
        return OfferingStorage.getByCode(code);
    }

    public void getOfferings() {
        // todo:
    }

    public Boolean doseHaveCapacity(OfferingEntity offeringEntity) {
        return offeringEntity.getRegistered() < offeringEntity.getCapacity();
    }

    public void addStudentToOffering(OfferingEntity offeringEntity) throws CapacityMismatchException {
        offeringEntity.setRegistered(offeringEntity.getRegistered() + 1);
        if (offeringEntity.getCapacity() < offeringEntity.getRegistered()) {
            throw new CapacityMismatchException(offeringEntity.getCode());
        }
    }
}
