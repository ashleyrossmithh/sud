package sud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import sud.domain.AttorneyPersonLink;

@Repository()
public interface AttorneyPersonLinkRepository extends JpaRepository<AttorneyPersonLink, Long>, JpaSpecificationExecutor<AttorneyPersonLink> {
}