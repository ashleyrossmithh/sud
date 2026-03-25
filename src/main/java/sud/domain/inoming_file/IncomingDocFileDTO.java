package sud.domain.inoming_file;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import sud.core.dto.support.BaseEntityDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Simple DTO for IncomingDocFile.
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public class IncomingDocFileDTO extends BaseEntityDTO<Long> {
    @JsonProperty("id")
    private Long id;

    private Long idOld;

    @JsonProperty("idfFileName")
    private String idfFileName;

    @JsonProperty("idfContent")
    private byte[] idfContent;

    @JsonProperty("idfNote")
    private String idfNote;

    private Long incomingDocId;

    @JsonIgnore
    public boolean isIdSet() {
        return id != null;
    }

    @JsonIgnore
    public boolean isIdOldSet() {
        return idOld != null;
    }

}
