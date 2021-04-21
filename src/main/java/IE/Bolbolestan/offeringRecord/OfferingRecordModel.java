package IE.Bolbolestan.offeringRecord;

import IE.Bolbolestan.bolbolestanExceptions.CapacityMismatchException;
import IE.Bolbolestan.bolbolestanExceptions.OfferingNotFoundException;
import IE.Bolbolestan.bolbolestanExceptions.OfferingRecordNotFoundException;
import IE.Bolbolestan.offering.OfferingEntity;
import IE.Bolbolestan.offering.OfferingModel;

import java.util.List;

public class OfferingRecordModel {
    public void addNewOfferingRecord(String studentId, String offeringCode, Double grade, String status) {
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

    public Boolean doesOfferingRecordExist(String studentId, String offeringRecord) {
        try {
            getOfferingRecord(studentId, offeringRecord);
            return true;
        } catch (OfferingRecordNotFoundException e) {
            return false;
        }
    }

    public Boolean isOfferingCompleted(String studentId, String offeringCode) throws OfferingRecordNotFoundException {
        return OfferingRecordStorage.getByCodeAndStudentId(
                studentId, offeringCode
        ).getStatus().equals(OfferingRecordEntity.COMPLETED_STATUS);
    }

    public void finalizeWaitList() throws OfferingNotFoundException, CapacityMismatchException {
        for (OfferingRecordEntity offeringRecordEntity: OfferingRecordStorage.getAllOfferings()) {
            OfferingEntity offeringEntity = new OfferingModel().getOffering(offeringRecordEntity.getOfferingCode());
            if (offeringRecordEntity.getStatus().equals(OfferingRecordEntity.FINALIZED_WAIT)) {
                if (offeringEntity.getRegistered() >= offeringEntity.getCapacity()) {
                    offeringEntity.setCapacity(offeringEntity.getCapacity() + 1);
                }
                offeringRecordEntity.setStatus(OfferingRecordEntity.FINALIZED_STATUS);
                new OfferingModel().addStudentToOffering(offeringEntity);
            }
        }
    }
}
