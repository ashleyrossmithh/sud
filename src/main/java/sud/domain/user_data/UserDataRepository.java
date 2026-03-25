package sud.domain.user_data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository()
public interface UserDataRepository extends JpaRepository<UserData, String> {

    UserData findByLogin(String login);

    @Query(value = """
            select ud from UserData ud where ud.person is not null and ud.person.id = ?1
            """)
    UserData findPersonById(Long personId);

    @Query(value = """
            delete from user_data ud where ud.login = ?1
            """, nativeQuery = true)
    void deleteByLogin(String login);
}
