package sud.rest.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sud.core.action.checkutils.Check;
import sud.core.util.LocalDateUtils;
import sud.domain.CourtSessionPersonLink;
import sud.domain.attorney.AttorneyDTO;
import sud.domain.court_session.CourtSession;
import sud.domain.court_session.CourtSessionDTO;
import sud.domain.court_session.CourtSessionDTOService;
import sud.domain.court_session.CourtSessionRepository;
import sud.domain.person.Person;
import sud.domain.person.PersonDTO;
import sud.domain.person.PersonRepository;
import sud.dto.AttorneyToPersonsDTO;
import sud.enums.DirectionType;
import sud.repositories.CourtSessionPersonLinkRepository;
import sud.services.PersonLinkService;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CourtSessionResource {
    private final CourtSessionDTOService courtSessionDTOService;
    private final CourtSessionPersonLinkRepository courtSessionPersonLinkRepository;
    private final PersonLinkService personLinkService;
    private final CourtSessionRepository courtSessionRepository;
    private final PersonRepository personRepository;

    @RequestMapping(
            value = {"/courtSession/create"},
            method = {RequestMethod.POST},
            produces = {"application/json"}
    )
    public ResponseEntity<Void> create(@RequestBody CourtSessionDTO courtSessionDTO) throws URISyntaxException {
        courtSessionDTOService.save(courtSessionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @RequestMapping(value = "/courtSession/{lawsuitId}", method = GET, produces = APPLICATION_JSON_VALUE)
    public List<CourtSessionDTO> getAllByLawsuitId(@PathVariable Long lawsuitId) throws URISyntaxException {
        List<CourtSessionDTO> courtSessionDTOList = courtSessionDTOService.getAllByLawsuitId(lawsuitId);
         for(CourtSessionDTO eachCourtSession: courtSessionDTOList) {
             List<CourtSessionPersonLink> links = courtSessionPersonLinkRepository.findAllByCourtSession_Id(eachCourtSession.getId());
             if (links != null && !links.isEmpty()) {
                 eachCourtSession.setHasAdvokate(true);
             }
         }
         return courtSessionDTOList;
    }

    @RequestMapping(value = "/courtSession/id/{courtSessionId}", method = GET, produces = APPLICATION_JSON_VALUE)
    public CourtSessionDTO getById(@PathVariable Long courtSessionId) throws URISyntaxException {
        CourtSessionDTO courtSessionDTO = courtSessionDTOService.findOne(courtSessionId);
        return courtSessionDTO;
    }

    @RequestMapping(value = "/courtSession/near/{personId}", method = GET, produces = APPLICATION_JSON_VALUE)
    public Check checkCourtSessionNear(@PathVariable Long personId) throws URISyntaxException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime near = now.plusDays(5);
        List<CourtSession> courtSessions = courtSessionRepository.findCourtSessionNearDate(now, near);
        Person p = personRepository.findById(personId).orElseThrow();
        if (DirectionType.BOSS != DirectionType.parse(p.getDirection())) {
            List<CourtSession> sessionsForPerson = courtSessions.stream().filter(cur -> courtSessionPersonLinkRepository.findByCourtSession_IdAndPerson_Id(cur.getId(), personId) != null).toList();
            if (!sessionsForPerson.isEmpty()) {
                String msg = sessionsForPerson.stream().map(cur -> cur.getBeginDateTime().format(DateTimeFormatter.ofPattern(LocalDateUtils.DATE_TIME_LONG_FORMAT))).collect(Collectors.joining());
                return Check.warning("Приближающиеся судебные заседания: " + msg);
            }
            return null;
        }
        if (!courtSessions.isEmpty()) {
            String msg = courtSessions.stream().map(cur -> cur.getBeginDateTime().format(DateTimeFormatter.ofPattern(LocalDateUtils.DATE_TIME_LONG_FORMAT))).collect(Collectors.joining());
            return Check.warning("Приближающиеся судебные заседания: " + msg);
        }
        return null;
    }

    @RequestMapping(value = "/loadPersonByCourtSessionId/{courtSessionId}", method = GET, produces = APPLICATION_JSON_VALUE)
    public List<PersonDTO> findById(@PathVariable Long courtSessionId) throws URISyntaxException {
        return personLinkService.loadPersonByCourtSessionId(courtSessionId);
    }
}
