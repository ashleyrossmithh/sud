package sud.domain;

import jakarta.persistence.Embeddable;
import lombok.Builder;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Builder
public class LawsuitPersonId implements Serializable {
    private Long lawsuitId;
    private Long personId;

    public LawsuitPersonId() {
    }

    public LawsuitPersonId(Long lawsuitId, Long personId) {
        this.lawsuitId = lawsuitId;
        this.personId = personId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LawsuitPersonId that = (LawsuitPersonId) o;
        return Objects.equals(lawsuitId, that.lawsuitId) && Objects.equals(personId, that.personId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lawsuitId, personId);
    }

    public Long getLawsuitId() {
        return lawsuitId;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setLawsuitId(Long lawsuitId) {
        this.lawsuitId = lawsuitId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }
}
