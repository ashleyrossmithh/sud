package sud.domain.incoming_doc;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository()
public interface IncomingDocRepository extends JpaRepository<IncomingDoc, Long>, JpaSpecificationExecutor<IncomingDoc> {

/*    default Long getNewId() {
        return getNextVal("S_INCOMING_DOC_FILE");
    }*/

}