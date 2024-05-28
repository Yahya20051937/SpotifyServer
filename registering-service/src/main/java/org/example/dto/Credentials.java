package org.example.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Data;

@Data
public class Credentials {
    private String type;
    private String value;

    private boolean temporary;


    public Credentials(UserRequest userRequest){
        this.type = "password";
        this.value = userRequest.getPassword();
        this.temporary = false;
    }
    public String toJson() throws JsonProcessingException {
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return writer.writeValueAsString(this);
    }
}
