package sud.domain;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@Embeddable
@Builder
public class HistoryStatusPersonId implements Serializable {
    private Long historyStatusId;
    private Long personId;
    public HistoryStatusPersonId() {}

    public HistoryStatusPersonId(Long historyStatusId, Long personId) {
        this.historyStatusId = historyStatusId;
        this.personId = personId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryStatusPersonId that = (HistoryStatusPersonId) o;
        return Objects.equals(historyStatusId, that.historyStatusId) && Objects.equals(personId, that.personId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(historyStatusId, personId);
    }

}
