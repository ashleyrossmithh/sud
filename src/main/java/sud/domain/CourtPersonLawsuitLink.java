package sud.domain;

import jakarta.persistence.*;
import lombok.Data;
import sud.domain.lawsuit.Lawsuit;

@Data
@Entity
@Table(name = "court_person_lawsuit_link")
//Судья относящийся к суду судебному делу (ведущий судебный процесс)
public class CourtPersonLawsuitLink {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lawsuit_id", nullable = false)
    private Lawsuit lawsuit;

    @JoinColumn(name = "court_person_link_id", nullable = false)
    private Long court_person_link_id;

    @Id
    @GeneratedValue
    private Long id;
}
