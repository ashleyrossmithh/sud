package sud.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginInfo {
    @JsonProperty("name")
    private String login;
    private String password;
}

