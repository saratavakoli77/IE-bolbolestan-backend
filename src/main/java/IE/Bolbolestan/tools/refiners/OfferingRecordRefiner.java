package IE.Bolbolestan.tools.refiners;

import IE.Bolbolestan.offeringRecord.OfferingRecordEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public class OfferingRecordRefiner {
    List<Map> offeringRecordListMap;
    private ObjectMapper mapper;

    public OfferingRecordRefiner(String data) throws JsonProcessingException {
        this.mapper = new ObjectMapper();
        this.offeringRecordListMap = this.mapper.readValue(data, new TypeReference<List<Map>>(){});
    }

    public List<OfferingRecordEntity> getRefinedEntities(String studentId) throws ParseException, JsonProcessingException {
        for (Map<String, Object> map: offeringRecordListMap) {
            this.refineOfferingCode(map);
            this.refineStudentId(map, studentId);
            this.refineStatus(map);
        }

        String json = new ObjectMapper().writeValueAsString(offeringRecordListMap);
        return mapper.readValue(json, new TypeReference<List<OfferingRecordEntity>>(){});
    }

    private void refineOfferingCode(Map<String, Object> offeringRecordMap) {
        offeringRecordMap.put("offeringCode", offeringRecordMap.get("code") + "01");
//        offeringRecordMap.remove("code");

    }

    private void refineStudentId(Map<String, Object> offeringRecordMap, String studentId) {
        offeringRecordMap.put("studentId", studentId);
    }

    private void refineStatus(Map<String, Object> offeringRecordMap) {
        offeringRecordMap.put("status", OfferingRecordEntity.COMPLETED_STATUS);
    }
}
