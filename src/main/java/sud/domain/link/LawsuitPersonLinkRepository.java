package sud.domain.link;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import sud.domain.LawsuitPersonId;
import sud.domain.LawsuitPersonLink;

@Repository()
public interface LawsuitPersonLinkRepository extends JpaRepository<LawsuitPersonLink, LawsuitPersonId>, JpaSpecificationExecutor<LawsuitPersonLink> {
}
