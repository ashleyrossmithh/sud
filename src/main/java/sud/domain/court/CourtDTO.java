package sud.domain.court;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.*;
import sud.core.dto.support.BaseEntityDTO;

@Getter
@Setter
@ToString
public class CourtDTO extends BaseEntityDTO<Long> {
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

    private Integer courtType;

    private String shortName;

    private String fullName;

    private String address;

    private String site;

    private String telNumber;
}
