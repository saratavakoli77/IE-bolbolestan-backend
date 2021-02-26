package bolbolestan.offering;

import java.util.ArrayList;
import java.util.List;

public class OfferingStorage {
    private static final List<OfferingEntity> offeringEntities = new ArrayList<>();

    public static void add(OfferingEntity offeringEntity) {
        offeringEntities.add(offeringEntity);
    }

    public static void remove(OfferingEntity offeringEntity) {
        offeringEntities.remove(offeringEntity);
    }

    public static OfferingEntity getByCode(String code) throws Exception {
        for (OfferingEntity entity: offeringEntities) {
            if (entity.getCode().equals(code)) {
                return entity;
            }
        }
        throw new Exception(); //todo proper exception
    }
}
