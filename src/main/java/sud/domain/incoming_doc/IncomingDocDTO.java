package sud.domain.incoming_doc;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sud.core.dto.support.BaseEntityDTO;

import java.time.LocalDate;


/**
 * Simple DTO for IncomingDocFile.
 */
@Getter
@Setter
@ToString
public class IncomingDocDTO extends BaseEntityDTO<Long> {
    @JsonProperty("id")
    private Long id;

    private Long idOld;
    private Long historyStatusId;
    private String idName;
    private String idDocNum;
    private LocalDate idDocDate;
    private String idNote;
    private String idCategory;
    private String idAuthor;
    private Boolean hasOriginal;

    @JsonIgnore
    public boolean isIdSet() {
        return id != null;
    }

    @JsonIgnore
    public boolean isIdOldSet() {
        return idOld != null;
    }

}
