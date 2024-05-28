package org.example.dto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Data;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.web.reactive.function.BodyInserter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class RegistrationRequestBody {
    private String username;
    private String email;

    private boolean enabled;


    private List<Credentials> credentials = new ArrayList<>();

    public RegistrationRequestBody(UserRequest userRequest) throws JsonProcessingException {
        this.username = userRequest.getUsername();
        this.email = userRequest.getEmail();
        this.enabled = true;
        this.credentials.add(new Credentials(userRequest));
    }

    public String toJson() throws JsonProcessingException {
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return writer.writeValueAsString(this);
    }
}
