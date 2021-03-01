package bolbolestan.offering;

import bolbolestan.bolbolestanExceptions.OfferingNotFoundException;

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

    public static OfferingEntity getByCode(String code) throws OfferingNotFoundException {
        for (OfferingEntity entity: offeringEntities) {
            if (entity.getCode().equals(code)) {
                return entity;
            }
        }
        throw new OfferingNotFoundException();
    }

    public static List<OfferingEntity> getAllOfferings() {
        return offeringEntities;
    }

    public static void removeAll() {
        offeringEntities.clear();
    }
}
