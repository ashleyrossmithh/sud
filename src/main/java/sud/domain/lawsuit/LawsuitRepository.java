package sud.domain.lawsuit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository()
public interface LawsuitRepository extends JpaRepository<Lawsuit, Long>, JpaSpecificationExecutor<Lawsuit> {

}
