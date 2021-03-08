package bolbolestan.offering;

import bolbolestan.bolbolestanExceptions.OfferingNotFoundException;
import bolbolestan.bolbolestanExceptions.StudentNotFoundException;

import java.util.HashMap;
import java.util.List;

import static java.lang.String.valueOf;

public class OfferingView {
    private OfferingModel model;

    public OfferingView() {
        model = new OfferingModel();
    }

    public HashMap<String, Object> getOfferingList()
            throws OfferingNotFoundException, StudentNotFoundException {
        List<OfferingEntity> allOfferings = model.getOfferings();
        HashMap<String, Object> data = new HashMap<>();

        for (OfferingEntity offeringEntity: allOfferings) {
            HashMap<String, String> offeringData = new HashMap<>();
            offeringData.put("courseCode", offeringEntity.getCode());
            offeringData.put("classCode", offeringEntity.getClassCode());
            offeringData.put("name", offeringEntity.getName());
            offeringData.put("units", valueOf(offeringEntity.getUnits()));
            offeringData.put("capacity", valueOf(offeringEntity.getCapacity()));
            offeringData.put("type", offeringEntity.getType());
            offeringData.put("days", offeringEntity.getType());
//            data.put(offeringEntity.getOfferingCode(), new HashMap<String, String>));
        }
        return data;

    }

    public HashMap<String, Object> getOfferingDetails(String offeringCode) throws OfferingNotFoundException {
        OfferingEntity offeringEntity = model.getOffering(offeringCode);
        HashMap<String, Object> data = new HashMap<>();
        data.put("offering", offeringEntity);
        return data;
    }
}
