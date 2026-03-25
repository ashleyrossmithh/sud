package sud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import sud.domain.CourtSessionPersonLink;
import sud.domain.person.Person;

import java.util.List;

@Repository()
public interface CourtSessionPersonLinkRepository extends JpaRepository<CourtSessionPersonLink, Long>, JpaSpecificationExecutor<CourtSessionPersonLink> {
    List<CourtSessionPersonLink> findAllByCourtSession_Id(Long courtSessionId);

    CourtSessionPersonLink findByCourtSession_IdAndPerson_Id(Long courtSessionId, Long personId);
}
