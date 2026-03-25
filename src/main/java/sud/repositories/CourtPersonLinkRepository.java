package sud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import sud.domain.CourtPersonLink;

@Repository()

public interface CourtPersonLinkRepository extends JpaRepository<CourtPersonLink, Long>, JpaSpecificationExecutor<CourtPersonLink> {
}
