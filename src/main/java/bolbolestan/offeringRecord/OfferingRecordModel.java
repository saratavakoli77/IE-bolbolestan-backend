package bolbolestan.offeringRecord;

import bolbolestan.bolbolestanExceptions.OfferingRecordNotFoundException;

import java.util.List;

public class OfferingRecordModel {
    public void addNewOfferingRecord(String studentId, String offeringCode, Double grade, String status) {
        //todo: delete later
        try {
            getOfferingRecord(studentId, offeringCode);
            System.out.println("in offering " + offeringCode + " vujud dare vase " + studentId);
            assert false;
        } catch (OfferingRecordNotFoundException e) {
            e.printStackTrace();
        }

        //todo: end

        OfferingRecordEntity offeringRecordEntity = new OfferingRecordEntity(studentId, offeringCode, grade, status);
        OfferingRecordStorage.add(offeringRecordEntity);
    }

    public void removeOfferingRecord(String studentId, String offeringCode) throws OfferingRecordNotFoundException {
        OfferingRecordEntity offeringRecordEntity = OfferingRecordStorage.getByCodeAndStudentId(studentId, offeringCode);
        OfferingRecordStorage.remove(offeringRecordEntity);
    }

    public void updateStatusOfferingRecord(String studentId, String offeringCode, String status)
            throws OfferingRecordNotFoundException {
        OfferingRecordEntity offeringRecordEntity =
                OfferingRecordStorage.getByCodeAndStudentId(studentId, offeringCode);
        offeringRecordEntity.setStatus(status);
    }

    public List<OfferingRecordEntity> getStudentOfferingRecords(String studentId) {
        return OfferingRecordStorage.getByStudentId(studentId);
    }

    public OfferingRecordEntity getOfferingRecord(String studentId, String offeringCode)
            throws OfferingRecordNotFoundException {
        return OfferingRecordStorage.getByCodeAndStudentId(studentId, offeringCode);
    }

    public Boolean isOfferingCompleted(String studentId, String offeringCode) throws OfferingRecordNotFoundException {
        return OfferingRecordStorage.getByCodeAndStudentId(
                studentId, offeringCode
        ).getStatus().equals(OfferingRecordEntity.COMPLETED_STATUS);
    }
}
