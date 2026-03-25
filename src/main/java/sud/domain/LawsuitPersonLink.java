package sud.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "lawsuit_person_link")
public class LawsuitPersonLink {

    @EmbeddedId
    private LawsuitPersonId id;

    private Boolean clientFlag;
}
