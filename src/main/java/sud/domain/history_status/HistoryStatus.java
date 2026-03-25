package sud.domain.history_status;

import jakarta.persistence.*;
import lombok.Data;
import sud.domain.lawsuit.Lawsuit;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "history_status")//статус смены судебного дела (HistoryStatusType)
public class HistoryStatus {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @JoinColumn(name = "lawsuit_id", nullable = false)
    @ManyToOne
    private Lawsuit lawsuit; // судебное дело

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private Integer code;

    private String note;

}
