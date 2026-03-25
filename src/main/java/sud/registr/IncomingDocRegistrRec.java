package sud.registr;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class IncomingDocRegistrRec {
    private Long incomingDocId;
    private Long lawsuitId;
    private String idAuthor;
    private String idCategory;
    private LocalDate idDocDate;
    private String idDocNum;
    private String idName;
    private String idNote;
    private Boolean hasOriginal;
    private Boolean hasFiles;

    public Long getId() {
        return this.incomingDocId;
    }
}
