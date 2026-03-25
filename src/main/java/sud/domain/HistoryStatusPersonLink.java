package sud.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "history_status_person_link")
// связь юриста с этапом ведения дела
public class HistoryStatusPersonLink {

    @EmbeddedId
    private HistoryStatusPersonId id;


}
