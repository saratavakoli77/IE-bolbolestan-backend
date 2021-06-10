package bolbolestan.offering;

import bolbolestan.bolbolestanExceptions.CapacityMismatchException;
import bolbolestan.bolbolestanExceptions.OfferingNotFoundException;
import bolbolestan.bolbolestanExceptions.StudentNotFoundException;

import java.util.ArrayList;
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

    public void removeStudentFromOffering(OfferingEntity offeringEntity) throws CapacityMismatchException {
        offeringEntity.setRegistered(offeringEntity.getRegistered() - 1);
        if (offeringEntity.getRegistered() < 0) {
            throw new CapacityMismatchException(offeringEntity.getOfferingCode());
        }
    }

    public List<OfferingEntity> getSearchResult(String searchValue) {
        List<OfferingEntity> offeringEntities = new ArrayList<>();
        for (OfferingEntity offeringEntity: OfferingStorage.getAllOfferings()) {
            if (offeringEntity.getName().contains(searchValue)) {
                offeringEntities.add(offeringEntity);
            }
        }
        return offeringEntities;
    }

}
