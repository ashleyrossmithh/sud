package sud.domain;


import jakarta.persistence.*;
import lombok.Data;
import sud.domain.court.Court;
import sud.domain.person.Person;

@Data
@Entity
@Table(name = "court_person_link")
//Судья относящийся к суду
public class CourtPersonLink {

    @JoinColumn(name = "court_id", nullable = false)
    @ManyToOne
    private Court court;

    @JoinColumn(name = "person_id", nullable = false)
    @ManyToOne
    private Person person;

    @Id
    @GeneratedValue
    private Long id;

}
