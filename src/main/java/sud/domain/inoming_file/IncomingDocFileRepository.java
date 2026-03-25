package sud.domain.inoming_file;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository()
public interface IncomingDocFileRepository extends JpaRepository<IncomingDocFile, Long>, JpaSpecificationExecutor<IncomingDocFile> {

/*    default Long getNewId() {
        return getNextVal("S_INCOMING_DOC_FILE");
    }*/

    List<IncomingDocFile> findAllByIncomingDocId(Long incomingDocId);
}