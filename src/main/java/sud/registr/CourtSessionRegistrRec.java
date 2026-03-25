package sud.registr;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CourtSessionRegistrRec {
    private Long courtSessionId;
    private Boolean finished;
    private LocalDateTime beginDateTime;
    private Long lawsuitId;
    private String zaName;
    private String otvName;
    private String lsName;
    private String lsNumber;
    private Integer statusCode;
    private String shortName;
    private String site;
    private String courtPersonFio;
    private String statusDesc;
    private Integer advocatCount;

    public Long getId() {
        return this.courtSessionId;
    }

    public Boolean getHasAdvocat() {//используется в ui
        return advocatCount > 0;
    }
}
