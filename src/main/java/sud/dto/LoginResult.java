package sud.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResult {
    private String errorMsg;
    private String fio;
    private Boolean adminFlag;
}
