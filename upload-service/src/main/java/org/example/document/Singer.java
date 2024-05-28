package org.example.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.SingerRequest;
import org.example.repository.SingerRepository;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document("singer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Singer {
    @MongoId
    private String id;
    private String name;

    public Singer(SingerRequest request){
        this.id = request.getId();
        this.name = request.getName();
    }

}
