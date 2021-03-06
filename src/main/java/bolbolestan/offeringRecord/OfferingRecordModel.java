package bolbolestan.offeringRecord;

import bolbolestan.bolbolestanExceptions.OfferingRecordNotFoundException;

public class OfferingRecordModel {
    public void addNewOfferingRecord(String studentId, String offeringCode, Integer grade, String status) {
        OfferingRecordEntity offeringRecordEntity = new OfferingRecordEntity(studentId, offeringCode, grade, status);
        OfferingRecordStorage.add(offeringRecordEntity);
    }

    public void removeOfferingRecord(String studentId, String offeringCode) throws OfferingRecordNotFoundException {
        OfferingRecordEntity offeringRecordEntity = OfferingRecordStorage.getByCodeAndStudentId(studentId, offeringCode);
        OfferingRecordStorage.remove(offeringRecordEntity);
    }

    public void updateStatusOfferingRecord(String studentId, String offeringCode, String status) throws OfferingRecordNotFoundException {
        OfferingRecordEntity offeringRecordEntity = OfferingRecordStorage.getByCodeAndStudentId(studentId, offeringCode);
        offeringRecordEntity.setStatus(status);
    } // todo : nemidunim vaghean avaz mishe ya na


}
