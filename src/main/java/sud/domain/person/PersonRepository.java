package sud.domain.person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sud.domain.lawsuit.Lawsuit;

import java.math.BigDecimal;
import java.util.List;

@Repository()
public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {
    List<Person> getAllByDirectionIn(List<Integer> directions);

    @Query(value = """
        SELECT p from Person p join LawsuitPersonLink lp on lp.id.personId = p.id and lp.id.lawsuitId = ?1
        where p.direction = ?2
    """)
    List<Person> getPersonByLawsuitIdAndDirection(Long lawsuitId, Integer direction);


    @Query(value = """
                select p
                 from Person p 
                         join CourtPersonLink pl on (p.id = pl.person.id and p.direction = 4) 
                 where pl.court.id = ?1  
             """)
    List<Person> loadCourtPersonByCourtId(Long courtId);

    @Query(value = """
                select p
                 from Person p join LawsuitPersonLink lpl on lpl.id.personId = p.id and lpl.id.lawsuitId = ?1
                  where p.direction = ?2
             """)
    List<Person> loadAdditionPersonByDirection(Long lawsuitId, Integer direction);

    @Query(value = """
            select p from Person p 
            join UserData u on u.person.id = p.id 
            join LawsuitPersonLink lpl on lpl.id.personId = p.id
             where lpl.id.lawsuitId = ?1
            """)
    Person findClientByLawsuitId(Long lawsuitId);

    @Query("""
            select l from Person p 
            join LawsuitPersonLink lpl on lpl.id.personId = p.id 
            join Lawsuit l on lpl.id.lawsuitId = l.id where p.id = ?1
            """)
    List<Lawsuit> findLawsuitByPersonId(Long personId);

    @Query(value = """
    select p
    from Person p
    join AttorneyPersonLink pl on p.id = pl.person.id
    where pl.attorney.id = ?1
    and p.direction in (0, 3, 5)""")
    List<Person> loadPersonByAttorneyId(Long attorneyId);

    @Query(value = """
    select p
    from Person p
    join HistoryStatusPersonLink hpl on p.id = hpl.id.personId
    where hpl.id.historyStatusId = ?1
    """)
    List<Person> loadPersonByHistoryStatusId(Long historyStatusId);

}
