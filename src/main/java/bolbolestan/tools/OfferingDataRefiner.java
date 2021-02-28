package bolbolestan.tools;


import bolbolestan.offering.OfferingEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

public class OfferingDataRefiner {
    Map<String, Object> offeringMap;
    private ObjectMapper mapper;

    public OfferingDataRefiner(String data) throws JsonProcessingException {
        this.mapper = new ObjectMapper();
        this.offeringMap = this.mapper.readValue(data, Map.class);
    }

    public OfferingEntity getRefinedOfferingEntity() throws ParseException, JsonProcessingException {
        this.refineClassTime();
        this.refineExamTime();
        System.out.println(offeringMap);
        String json = new ObjectMapper().writeValueAsString(offeringMap);
        return mapper.readValue(json, OfferingEntity.class);
    }

    private void refineClassTime() throws ParseException {
        Map<String, Object> classTimeMap = (Map) offeringMap.get("classTime");
        offeringMap.put("classTimeDays", classTimeMap.get("days"));
        String classTimeRange = (String) classTimeMap.get("time");
        Map<String, Date> dateMap = DateParser.getDatesFromString(classTimeRange);
        offeringMap.put("classTimeStart", dateMap.get("start"));
        offeringMap.put("classTimeEnd", dateMap.get("end"));
        offeringMap.remove("classTime");
    }

    private void refineExamTime() throws ParseException {
        Map<String, Object> examTimeMap = ((Map) offeringMap.get("examTime"));
        offeringMap.put(
                "examTimeStart", DateParser.getDateFromExamFormat((String) examTimeMap.get("start"))
        );

        offeringMap.put(
                "examTimeEnd", DateParser.getDateFromExamFormat((String) examTimeMap.get("end"))
        );

        offeringMap.remove("examTime");
    }
}
