package sud.domain.attorney;

import jakarta.persistence.*;
import lombok.Data;
import sud.domain.lawsuit.Lawsuit;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "attorney") //доверенность
public class Attorney implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @JoinColumn(name = "lawsuit_id", nullable = false)
    @ManyToOne
    private Lawsuit lawsuit; // судебное дело

    private Long fromPersonId;

    private LocalDate beginDate;

    private LocalDate endDate;

    private String number;

    private String description;

}
