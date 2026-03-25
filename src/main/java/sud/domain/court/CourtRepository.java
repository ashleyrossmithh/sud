package sud.domain.court;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository()
public interface CourtRepository extends JpaRepository<Court, Long>, JpaSpecificationExecutor<Court> {
}
