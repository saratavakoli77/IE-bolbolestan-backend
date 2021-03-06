package bolbolestan.offeringRecord;

import bolbolestan.bolbolestanExceptions.OfferingRecordNotFoundException;

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

//    public static OfferingRecordEntity getByCode(String code) throws OfferingRecordNotFoundException {
//        for (OfferingRecordEntity entity: offeringRecordEntities) {
//            if (entity.getOfferingCode().equals(code)) {
//                return entity;
//            }
//        }
//        throw new OfferingRecordNotFoundException();
//    }

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
}
