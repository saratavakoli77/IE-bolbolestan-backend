package bolbolestan.offering;

public class OfferingModel {
    public void addNewOffering(OfferingEntity offeringEntity) {
        //todo: validate data
        OfferingStorage.add(offeringEntity);
    }
}
