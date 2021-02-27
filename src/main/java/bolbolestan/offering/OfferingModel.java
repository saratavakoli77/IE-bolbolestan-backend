package bolbolestan.offering;

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
}
