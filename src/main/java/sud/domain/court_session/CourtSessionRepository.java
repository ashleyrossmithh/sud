package sud.domain.court_session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository()
public interface CourtSessionRepository extends JpaRepository<CourtSession, Long>, JpaSpecificationExecutor<CourtSession> {
    List<CourtSession> getAllByLawsuitId(Long lawsuitId);

    @Query(value = """
        select c from CourtSession c 
        where c.beginDateTime between ?1 and ?2
    """)
    List<CourtSession> findCourtSessionNearDate(LocalDateTime localDateTimeBegin, LocalDateTime localDateTimeEnd);
}
