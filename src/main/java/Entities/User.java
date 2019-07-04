
package Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "data")
@lombok.Data
@SuppressWarnings("unused")
public class User {

    private String avatar;
    private String email;
    @JsonProperty("first_name")
    private String firstName;
    private long id;
    @JsonProperty("last_name")
    private String lastName;

}
