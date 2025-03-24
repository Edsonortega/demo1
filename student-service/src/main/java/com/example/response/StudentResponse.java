package com.example.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

//@Getter
//@Setter
//@ToString
//@EqualsAndHashCode
@Data
//@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {
    @JsonIgnore
    private long id;
    @JsonProperty("amo OP")
    private String firstName;
    private String lastName;

}
