package IE.Bolbolestan.offeringRecord;


import IE.Bolbolestan.bolbolestanExceptions.OfferingRecordNotFoundException;
import IE.Bolbolestan.student.StudentEntity;
import IE.Bolbolestan.student.StudentStorage;
import IE.Bolbolestan.tools.HttpClient;
import IE.Bolbolestan.tools.refiners.OfferingRecordRefiner;

import java.util.ArrayList;
import java.util.List;

public class OfferingRecordStorage {
    private static final List<OfferingRecordEntity> offeringRecordEntities = new ArrayList<>();

    public static void add(OfferingRecordEntity offeringRecordEntityEntity) {
        offeringRecordEntities.add(offeringRecordEntityEntity);
    }

    public static void remove(OfferingRecordEntity offeringRecordEntityEntity) {
        offeringRecordEntities.remove(offeringRecordEntityEntity);
    }

    public static List<OfferingRecordEntity> getByStudentId(String studentId) {
        List<OfferingRecordEntity> studentOfferingRecordEntities = new ArrayList<>();
        for (OfferingRecordEntity entity: offeringRecordEntities) {
            if (entity.getStudentId().equals(studentId)) {
                studentOfferingRecordEntities.add(entity);
            }
        }
        return studentOfferingRecordEntities;
    }

    public static OfferingRecordEntity getByCodeAndStudentId(String studentId, String code)
            throws OfferingRecordNotFoundException {
        for (OfferingRecordEntity entity: offeringRecordEntities) {
            if (entity.getStudentId().equals(studentId) && entity.getOfferingCode().equals(code)) {
                return entity;
            }
        }
        throw new OfferingRecordNotFoundException();
    }

    public static List<OfferingRecordEntity> getAllOfferings() {
        return offeringRecordEntities;
    }

    public static void removeAll() {
        offeringRecordEntities.clear();
    }

    public static List<OfferingRecordEntity> getAllOfferingRecords() {
        return offeringRecordEntities;
    }

    public static void getDataFromApi() {
        HttpClient http = new HttpClient();
        String fetchUrl;
        for (StudentEntity studentEntity: StudentStorage.getAllStudents()) {
            fetchUrl = "grades/" + studentEntity.getStudentId();
            try {
                String response = http.get(fetchUrl);
                List<OfferingRecordEntity> offeringRecords =
                        new OfferingRecordRefiner(response).getRefinedEntities(studentEntity.getStudentId());
                offeringRecordEntities.addAll(offeringRecords);
                System.out.println("Fetched " + offeringRecords.size() + " records");
            } catch (Exception e) {
                System.out.println("error");
                e.fillInStackTrace();
            }
        }

    }
}
