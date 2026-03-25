package sud.domain;

import jakarta.persistence.*;
import lombok.Data;
import sud.domain.attorney.Attorney;
import sud.domain.person.Person;


@Data
@Entity
@Table(name = "attorney_person_link")
// связь адваката с доверенностью
public class AttorneyPersonLink {

    @JoinColumn(name = "attorney_id", nullable = false)
    @ManyToOne
    private Attorney attorney;

    @JoinColumn(name = "person_id", nullable = false)
    @ManyToOne
    private Person person;

    @Id
    @GeneratedValue
    private Long id;
}
