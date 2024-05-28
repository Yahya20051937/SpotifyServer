package org.example.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class Role {
    private String id;
    private String name;
    private String description;
    private boolean composite;
    private boolean clientRole;
    private String containerId;

    public Role(String roleJson) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(roleJson, Map.class);
        this.id = (String) map.get("id");
        this.name = (String) map.get("name");
        this.composite = (boolean) map.get("composite");
        this.clientRole = (boolean) map.get("clientRole");
        this.containerId = (String) map.get("containerId");


    }

    public String toJson() throws JsonProcessingException {
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = writer.writeValueAsString(this);
        return "[\n" + json + "\n]";
    }
}
