package sud.domain.attorney;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sud.domain.person.Person;

import java.time.LocalDate;
import java.util.List;

@Repository()
public interface AttorneyRepository extends JpaRepository<Attorney, Long>, JpaSpecificationExecutor<Attorney> {
    List<Attorney> getAllByLawsuitId(Long lawsuitId);

    @Query(value = """
    select p from Attorney a 
        join AttorneyPersonLink ap on a.id = ap.attorney.id 
        join Person p on ap.person.id = p.id
    where a.lawsuit.id = ?1
    and a.endDate > ?2
    """)
    List<Person> getPersonByLawsuitId(Long lawsuitId, LocalDate courtSession);
}
