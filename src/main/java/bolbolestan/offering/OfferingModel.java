package bolbolestan.offering;

import bolbolestan.bolbolestanExceptions.CapacityMismatchException;
import bolbolestan.bolbolestanExceptions.OfferingNotFoundException;
import bolbolestan.bolbolestanExceptions.StudentNotFoundException;

import java.util.List;

public class OfferingModel {
    public void addNewOffering(OfferingEntity offeringEntity) {
        //todo: validate data
        OfferingStorage.add(offeringEntity);
    }

    public OfferingEntity getOffering(String code) throws OfferingNotFoundException {
        return OfferingStorage.getByCode(code);
    }

    public List<OfferingEntity> getOfferings() {
        return OfferingStorage.getAllOfferings();
    }

    public Boolean doseHaveCapacity(OfferingEntity offeringEntity) {
        return offeringEntity.getRegistered() < offeringEntity.getCapacity();
    }

    public void addStudentToOffering(OfferingEntity offeringEntity) throws CapacityMismatchException {
        offeringEntity.setRegistered(offeringEntity.getRegistered() + 1);
        if (offeringEntity.getCapacity() < offeringEntity.getRegistered()) {
            throw new CapacityMismatchException(offeringEntity.getOfferingCode());
        }
    }
}
