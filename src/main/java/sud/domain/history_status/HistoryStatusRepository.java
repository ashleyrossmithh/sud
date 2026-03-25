package sud.domain.history_status;

import lombok.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository()
public interface HistoryStatusRepository extends JpaRepository<HistoryStatus, Long>, JpaSpecificationExecutor<HistoryStatus> {
    List<HistoryStatus> getAllByLawsuitId(Long lawsuitId);

    @Query(value = """
        select h from HistoryStatus h
        where h.lawsuit.id = ?1
        and h.startDateTime = (select max(h2.startDateTime) from HistoryStatus h2 where h2.lawsuit.id = ?1)
    """)
    HistoryStatus getLastHistoryStatus(Long lawsuitId);
}