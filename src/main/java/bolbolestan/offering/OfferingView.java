package bolbolestan.offering;

import bolbolestan.bolbolestanExceptions.OfferingNotFoundException;
import bolbolestan.course.DaysOfWeek;
import bolbolestan.tools.DateParser;

import java.util.HashMap;
import java.util.List;

import static java.lang.String.valueOf;

public class OfferingView {
    private OfferingModel model;

    public OfferingView() {
        model = new OfferingModel();
    }

    public HashMap<String, Object> getOfferingList()  {
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
            offeringData.put("days", getStringDays(offeringEntity, "|"));
            offeringData.put(
                    "time",
                    DateParser.getStringFromDates(
                            offeringEntity.getClassTimeStart(), offeringEntity.getClassTimeEnd())
            );
            offeringData.put("examStart", DateParser.getStringFromExamDate(offeringEntity.getExamTimeStart()));
            offeringData.put("examEnd", DateParser.getStringFromExamDate(offeringEntity.getExamTimeEnd()));
            offeringData.put("prerequisites", String.join("|", offeringEntity.getPrerequisites()));
            offeringData.put("link", "/course/" + offeringEntity.getCode() + "/" + offeringEntity.getClassCode());
            data.put(offeringEntity.getOfferingCode(), offeringData);
        }
        return data;

    }

    private String getStringDays(OfferingEntity offeringEntity, String delimiter) {
        StringBuilder dayList = new StringBuilder();
        for (DaysOfWeek day: offeringEntity.getClassTimeDays()) {
            dayList.append(day.toString()).append(delimiter);
        }
        return dayList.toString();
    }

    public HashMap<String, Object> getOfferingDetail(String offeringCode) throws OfferingNotFoundException {
        OfferingEntity offeringEntity = model.getOffering(offeringCode);
        HashMap<String, Object> data = new HashMap<>();
        data.put("courseCode", offeringEntity.getCode());
        data.put("classCode", offeringEntity.getClassCode());
        data.put("units", valueOf(offeringEntity.getUnits()));
        data.put("days", getStringDays(offeringEntity, ", "));
        data.put(
                "time",
                DateParser.getStringFromDates(
                        offeringEntity.getClassTimeStart(), offeringEntity.getClassTimeEnd())
        );
        return data;
    }
}
