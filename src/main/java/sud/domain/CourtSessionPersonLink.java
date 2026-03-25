package sud.domain;

import jakarta.persistence.*;
import lombok.Data;
import sud.domain.court_session.CourtSession;
import sud.domain.person.Person;

@Data
@Entity
@Table(name = "court_session_person_link")
//связь адвоката с судебным заседанием
public class CourtSessionPersonLink {

    @JoinColumn(name = "court_session_id", nullable = false)
    @ManyToOne
    private CourtSession courtSession;

    @JoinColumn(name = "person_id", nullable = false)
    @ManyToOne
    private Person person;

    @Id
    @GeneratedValue
    private Long id;
}
