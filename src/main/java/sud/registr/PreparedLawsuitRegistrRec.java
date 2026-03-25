package sud.registr;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class PreparedLawsuitRegistrRec {
    private Long id;
    private Boolean finished;
    private Integer code;
    private LocalDateTime startDateTime;
    private Long lawsuitId;
    private String zaName;
    private String otvName;
    private String lsName;
    private String lsNumber;
    private Integer responseCount;
    private String statusDesc;

    public Boolean getHasResponseble() {//используется в ui
        return responseCount > 0;
    }
}
