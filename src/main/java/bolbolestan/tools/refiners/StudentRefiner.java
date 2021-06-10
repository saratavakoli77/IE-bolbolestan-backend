package bolbolestan.tools.refiners;

import bolbolestan.student.StudentEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class StudentRefiner {
    List<Map> studentListMap;
    private ObjectMapper mapper;

    public StudentRefiner(String data) throws JsonProcessingException {
        this.mapper = new ObjectMapper();
        this.studentListMap = this.mapper.readValue(data, new TypeReference<List<Map>>(){});
    }

    public List<StudentEntity> getRefinedEntities() throws JsonProcessingException {
        String json = new ObjectMapper().writeValueAsString(studentListMap);
        return mapper.readValue(json, new TypeReference<List<StudentEntity>>(){});
    }
}
