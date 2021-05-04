package IE.Bolbolestan.offering;

import IE.Bolbolestan.bolbolestanExceptions.CapacityMismatchException;
import IE.Bolbolestan.bolbolestanExceptions.OfferingNotFoundException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OfferingModel {
    public OfferingEntity getOffering(String code) throws OfferingNotFoundException {
        List<String> offeringTableKeys = new ArrayList<>();
        offeringTableKeys.add(code.substring(0, code.length() - 2));
        offeringTableKeys.add(code.substring(code.length() - 2));
        try {
            return OfferingRepository.getInstance().getById(offeringTableKeys);
        } catch (SQLException throwables) {
            throw new OfferingNotFoundException();
        }
    }

    public List<OfferingEntity> getOfferings() {
        try {
            return OfferingRepository.getInstance().getAll();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public Boolean doseHaveCapacity(OfferingEntity offeringEntity) {
        return offeringEntity.getRegistered() < offeringEntity.getCapacity();
    }

    public void addStudentToOffering(OfferingEntity offeringEntity)
            throws CapacityMismatchException, SQLException {
        offeringEntity.setRegistered(offeringEntity.getRegistered() + 1);
        OfferingRepository.getInstance().updateObjectRegistered(offeringEntity);
        if (offeringEntity.getCapacity() < offeringEntity.getRegistered()) {
            throw new CapacityMismatchException(offeringEntity.getOfferingCode());
        }
    }

    public void removeStudentFromOffering(OfferingEntity offeringEntity)
            throws CapacityMismatchException, SQLException {
        offeringEntity.setRegistered(offeringEntity.getRegistered() - 1);
        OfferingRepository.getInstance().updateObjectRegistered(offeringEntity);
        if (offeringEntity.getRegistered() < 0) {
            throw new CapacityMismatchException(offeringEntity.getOfferingCode());
        }
    }

    public List<OfferingEntity> getSearchResult(String searchValue) {
        List<OfferingEntity> offeringEntities = new ArrayList<>();
        try {
            for (OfferingEntity offeringEntity: OfferingRepository.getInstance().getAll()) {
                if (offeringEntity.getName().contains(searchValue)) {
                    offeringEntities.add(offeringEntity);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return offeringEntities;
    }

}
