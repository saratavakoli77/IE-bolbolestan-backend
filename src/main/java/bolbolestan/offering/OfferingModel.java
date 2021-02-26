package bolbolestan.offering;

public class OfferingModel {
    public void addNewOffering(OfferingEntity offeringEntity) {
        //todo: validate data
        OfferingStorage.add(offeringEntity);
    }

    public OfferingEntity getOffering(String code) {
        try {
            return OfferingStorage.getByCode(code);
        } catch (Exception e) {//todo proper exception
            e.printStackTrace();
        }
        return null;
    }

    public void getOfferings() {
        // todo:
    }
}
