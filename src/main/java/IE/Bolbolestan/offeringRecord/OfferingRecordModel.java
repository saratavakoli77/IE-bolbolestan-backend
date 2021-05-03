package IE.Bolbolestan.offeringRecord;

import IE.Bolbolestan.bolbolestanExceptions.CapacityMismatchException;
import IE.Bolbolestan.bolbolestanExceptions.OfferingNotFoundException;
import IE.Bolbolestan.bolbolestanExceptions.OfferingRecordNotFoundException;
import IE.Bolbolestan.offering.OfferingEntity;
import IE.Bolbolestan.offering.OfferingModel;

import java.sql.SQLException;
import java.util.List;

public class OfferingRecordModel {
    public void addNewOfferingRecord(String studentId, String offeringCode, Double grade, String status) {
        OfferingRecordEntity offeringRecordEntity = new OfferingRecordEntity(studentId, offeringCode, grade, status);
        try {
            OfferingRecordRepository.getInstance().insert(offeringRecordEntity);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void removeOfferingRecord(String studentId, String offeringCode) throws OfferingRecordNotFoundException, SQLException {
        OfferingRecordEntity offeringRecordEntity = OfferingRecordRepository.getInstance().getByCodeAndStudentId(studentId, offeringCode);
        OfferingRecordRepository.getInstance().deleteEntity(offeringRecordEntity);
    }

    public void updateStatusOfferingRecord(String studentId, String offeringCode, String status)
            throws OfferingRecordNotFoundException {
        OfferingRecordEntity offeringRecordEntity = null;
        try {
            offeringRecordEntity = OfferingRecordRepository.getInstance().getByCodeAndStudentId(studentId, offeringCode);
            offeringRecordEntity.setStatus(status);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<OfferingRecordEntity> getStudentOfferingRecords(String studentId) {
        try {
            return OfferingRecordRepository.getInstance().getByStudentId(studentId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public OfferingRecordEntity getOfferingRecord(String studentId, String offeringCode)
            throws OfferingRecordNotFoundException {
        try {
            return OfferingRecordRepository.getInstance().getByCodeAndStudentId(studentId, offeringCode);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public Boolean doesOfferingRecordExist(String studentId, String offeringRecord) {
        try {
            getOfferingRecord(studentId, offeringRecord);
            return true;
        } catch (OfferingRecordNotFoundException e) {
            return false;
        }
    }

    public Boolean isOfferingCompleted(String studentId, String offeringCode) throws OfferingRecordNotFoundException, SQLException {
        return OfferingRecordRepository.getInstance().getByCodeAndStudentId(
                studentId, offeringCode
        ).getStatus().equals(OfferingRecordEntity.COMPLETED_STATUS);
    }

    public void finalizeWaitList() throws OfferingNotFoundException, CapacityMismatchException, SQLException {
        for (OfferingRecordEntity offeringRecordEntity: OfferingRecordRepository.getInstance().getAll()) {
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
