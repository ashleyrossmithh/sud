package sud.domain.inoming_file;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "incoming_doc_file")// документы в эл. виде
public class IncomingDocFile implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;
    private Long incomingDocId;
    private String idfFileName;
    private byte[] idfContent;
    private String idfNote;

/*    // Many to one
    private IncomingDoc incomingDoc;

    @JoinColumn(name = "INCOMING_DOC_ID", nullable = false)
    @ManyToOne
    public IncomingDoc getIncomingDoc() {
        return incomingDoc;
    }*/
}
