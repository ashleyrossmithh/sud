package sud.services;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sud.domain.CourtSessionPersonLink;
import sud.domain.court.CourtRepository;
import sud.domain.court_session.CourtSessionRepository;
import sud.domain.person.PersonDTO;
import sud.domain.person.PersonDTOService;
import sud.domain.CourtPersonLink;
import sud.domain.person.PersonRepository;
import sud.repositories.CourtPersonLinkRepository;
import sud.repositories.CourtSessionPersonLinkRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonLinkService {
    private final CourtPersonLinkRepository personLinkRepository;
    private final CourtSessionPersonLinkRepository courtSessionPersonLinkRepository;

    private final PersonDTOService personDTOService;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final PersonRepository personRepository;
    private final CourtRepository courtRepository;
    private final CourtSessionRepository courtSessionRepository;

    @Transactional
    public void createCourtPersonLink(PersonDTO personDto, Long courtId) {
        personDto = personDTOService.save(personDto);
        CourtPersonLink link = new CourtPersonLink();
        link.setPerson(personRepository.getReferenceById(personDto.getId()));
        link.setCourt(courtRepository.getReferenceById(courtId));
        this.personLinkRepository.save(link);
    }

    @Transactional
    public void createCourtSessionPersonLink(List<Long> personIds, Long courtSessionId) {
        for (Long personId: personIds) {
            CourtSessionPersonLink link = new CourtSessionPersonLink();
            link.setPerson(personRepository.getReferenceById(personId));
            link.setCourtSession(courtSessionRepository.getReferenceById(courtSessionId));
            this.courtSessionPersonLinkRepository.save(link);
        }
    }

    public List<PersonDTO> loadPersonByCourtSessionId(Long courtSessionId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("FilterCourtSessionId", courtSessionId);
        return jdbcTemplate.query("""                             
                select
                    adv.first_name,
                    adv.patronymic,
                    adv.surname,
                    adv.id
                from person adv 
                        join court_session_person_link pl on (adv.id = pl.person_id) 
                where pl.court_session_id = :FilterCourtSessionId               
                and adv.direction in (0, 3, 5)
                """, namedParameters, new DataClassRowMapper<>(PersonDTO.class));
    }
}
