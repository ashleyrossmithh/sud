package sud.domain.history_status;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sud.core.dto.support.BaseEntityDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class HistoryStatusDTO extends BaseEntityDTO<Long> {
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

    private Long lawsuitId; // судебное дело

    private LocalDateTime startDate;

    private Integer code;

    private String note;

    private String name;

    private String lsRegNum;

    private Long personId;

    private List<Long> responseblePersonIds;

    private String time;
}
