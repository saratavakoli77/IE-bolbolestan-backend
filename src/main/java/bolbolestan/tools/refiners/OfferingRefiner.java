package bolbolestan.tools.refiners;

import bolbolestan.offering.OfferingEntity;
import bolbolestan.tools.DateParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class OfferingRefiner {
    List<Map> offeringListMap;
    private ObjectMapper mapper;

    public OfferingRefiner(String data) throws JsonProcessingException {
        this.mapper = new ObjectMapper();
        this.offeringListMap = this.mapper.readValue(data, new TypeReference<List<Map>>(){});
    }

    public List<OfferingEntity> getRefinedEntities() throws ParseException, JsonProcessingException {
        for (Map<String, Object> map: offeringListMap) {
            this.refineClassTime(map);
            this.refineExamTime(map);
        }

        String json = new ObjectMapper().writeValueAsString(offeringListMap);
        return mapper.readValue(json, new TypeReference<List<OfferingEntity>>(){});
    }

    private void refineClassTime(Map<String, Object> offeringMap) throws ParseException {
        Map<String, Object> classTimeMap = (Map) offeringMap.get("classTime");
        offeringMap.put("classTimeDays", classTimeMap.get("days"));
        String classTimeRange = (String) classTimeMap.get("time");
        Map<String, Date> dateMap = DateParser.getDatesFromString(classTimeRange);
        offeringMap.put("classTimeStart", dateMap.get("start"));
        offeringMap.put("classTimeEnd", dateMap.get("end"));
        offeringMap.remove("classTime");
    }

    private void refineExamTime(Map<String, Object> offeringMap) throws ParseException {
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
