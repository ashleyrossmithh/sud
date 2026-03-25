package sud.domain.court_session;

import jakarta.persistence.*;
import lombok.Data;
import sud.domain.lawsuit.Lawsuit;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "court_session")//судебное заседание
public class CourtSession implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @JoinColumn(name = "lawsuit_id", nullable = false)
    @ManyToOne
    private Lawsuit lawsuit; // судебное дело

    private LocalDateTime beginDateTime;

    private String note;

    private String result;

    private Boolean needPresence;

}
