package bolbolestan.offering;

import bolbolestan.bolbolestanExceptions.OfferingNotFoundException;
import bolbolestan.tools.HttpClient;
import bolbolestan.tools.refiners.OfferingRefiner;

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

    public static void getDataFromApi() {
        HttpClient http = new HttpClient();
        String fetchProjectsUrl = "courses";

        try {
            String response = http.get(fetchProjectsUrl);
            List<OfferingEntity> offerings = new OfferingRefiner(response).getRefinedEntities();
            offeringEntities.addAll(offerings);
            System.out.println("Fetched " + offerings.size() + " courses");
        } catch (Exception e) {
            System.out.println("error");
            e.fillInStackTrace();
        }
    }
}
