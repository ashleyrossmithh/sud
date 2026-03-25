package sud.domain.link;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import sud.domain.HistoryStatusPersonId;
import sud.domain.HistoryStatusPersonLink;

@Repository()
public interface HistoryStatusPersonLinkRepository extends JpaRepository<HistoryStatusPersonLink, HistoryStatusPersonId>, JpaSpecificationExecutor<HistoryStatusPersonLink> {
}
