package sud.domain.court_session;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sud.core.dto.support.BaseEntityDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CourtSessionDTO extends BaseEntityDTO<Long> {
    @JsonProperty("id")
    private Long id;

    private Long idOld;

    @JsonIgnore
    public boolean isIdSet() {
        return id != null;
    }

    @JsonIgnore
    public boolean isIdOldSet() {
        return idOld != null;
    }


    private Long lawsuitId;

    private LocalDateTime beginDateTime;

    private String note;

    private String result;

    private Boolean hasAdvokate = false;

    private Boolean needPresence = false;

}
