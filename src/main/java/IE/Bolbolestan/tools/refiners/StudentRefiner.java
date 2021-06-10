package IE.Bolbolestan.tools.refiners;

import IE.Bolbolestan.student.StudentEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class StudentRefiner {
    List<Map> studentListMap;
    private ObjectMapper mapper;

    public StudentRefiner(String data) throws JsonProcessingException {
        this.mapper = new ObjectMapper();
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.studentListMap = this.mapper.readValue(data, new TypeReference<List<Map>>(){});
    }

    public List<StudentEntity> getRefinedEntities() throws JsonProcessingException {
        String json = new ObjectMapper().writeValueAsString(studentListMap);
        for (Map<String, Object> map: studentListMap) {
            if (map.containsKey("email")) {
                map.remove("email");
            }

            if (map.containsKey("password")) {
                map.remove("password");
            }
        }
        return mapper.readValue(json, new TypeReference<List<StudentEntity>>(){});
    }
}
