package bolbolestan.tools;

import bolbolestan.course.DaysOfWeek;
import bolbolestan.offering.OfferingEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetOfferingDataRefiner {
    Map<String, Object> offeringMap;
    private ObjectMapper mapper;

    public Map<String, Object> getRefinedOfferingData(OfferingEntity offeringEntity) throws JsonProcessingException {
        this.mapper = new ObjectMapper();
        this.offeringMap = this.mapper.convertValue(offeringEntity, Map.class);
        this.refineClassTime();
        this.refineExamTime();
        this.refineRegistered();
        return this.offeringMap;
    }

    private void refineExamTime() {
        Date examTimeStartDate = new Date((Long) offeringMap.get("examTimeStart"));
        Date examTimeEndDate = new Date((Long) offeringMap.get("examTimeEnd"));
        offeringMap.remove("examTimeStart");
        offeringMap.remove("examTimeEnd");

        Map<String, String> examDateMap = new HashMap<>();
        examDateMap.put("start", DateParser.getStringFromExamDate(examTimeStartDate));
        examDateMap.put("end", DateParser.getStringFromExamDate(examTimeEndDate));
        offeringMap.put("examTime", examDateMap);
    }

    private void refineClassTime() {
        List<DaysOfWeek> classTimeDays = (List<DaysOfWeek>) offeringMap.get("classTimeDays");
        Date classTimeStartDate = new Date((Long) offeringMap.get("classTimeStart"));
        Date classTimeEndDate = new Date((Long) offeringMap.get("classTimeEnd"));
        offeringMap.remove("classTimeDays");
        offeringMap.remove("classTimeStart");
        offeringMap.remove("classTimeEnd");

        Map<String, Object> classTimeMap = new HashMap<>();
        classTimeMap.put("days", classTimeDays);
        classTimeMap.put("time", DateParser.getStringFromDates(classTimeStartDate, classTimeEndDate));
        offeringMap.put("classTime", classTimeMap);
    }

    private void refineRegistered() {
        offeringMap.remove("registered");
    }
}
