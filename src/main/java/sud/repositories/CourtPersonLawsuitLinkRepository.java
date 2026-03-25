package sud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import sud.domain.CourtPersonLawsuitLink;

@Repository()
public interface CourtPersonLawsuitLinkRepository extends JpaRepository<CourtPersonLawsuitLink, Long>, JpaSpecificationExecutor<CourtPersonLawsuitLink> {
}
