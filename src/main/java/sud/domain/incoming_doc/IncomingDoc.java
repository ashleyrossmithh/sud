package sud.domain.incoming_doc;

import jakarta.persistence.*;
import lombok.Data;
import sud.domain.history_status.HistoryStatus;
import sud.domain.lawsuit.Lawsuit;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "incoming_doc")//документы
public class IncomingDoc implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    @Column(name = "INCOMING_DOC_ID")
    private Long id;

    @JoinColumn(name = "history_status_id", nullable = false)
    @ManyToOne
    private HistoryStatus historyStatus; // этап судебного разбирательства

    private String idName;
    private String idDocNum;
    private LocalDate idDocDate;
    private String idNote;
    private String idCategory;
    private String idAuthor;
    private Boolean hasOriginal;
}
