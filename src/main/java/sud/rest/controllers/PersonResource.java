package sud.rest.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sud.core.rest.support.CanCompleteResource;
import sud.core.rest.support.CanUpdateResource;
import sud.domain.attorney.AttorneyRepository;
import sud.domain.court_session.CourtSessionRepository;
import sud.domain.person.Person;
import sud.domain.person.PersonDTO;
import sud.domain.person.PersonDTOService;
import sud.domain.person.PersonRepository;
import sud.enums.DirectionType;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api/entities/person")
@Log4j2
@RequiredArgsConstructor
public class PersonResource implements CanUpdateResource<PersonDTO, PersonDTOService, Person, PersonRepository, Long>,
        CanCompleteResource<PersonDTO, PersonDTOService> {
    private final PersonDTOService personDTOService;
    private final PersonRepository personRepository;
    private final AttorneyRepository attorneyRepository;
    private final CourtSessionRepository courtSessionRepository;

    @Override
    public String getApiUrl() {
        return "/api/entities/person/";
    }

    @Override
    public PersonDTOService getDTOService() {
        return personDTOService;
    }

    @RequestMapping(value = "/{id:.+}", method = DELETE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("Delete by id Court : {}", id);
        getDTOService().deleteById(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id:.+}", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonDTO> findById(@PathVariable Long id) throws URISyntaxException {
        log.debug("Find by id CourtDTO : {}", id);
        return Optional.ofNullable(getDTOService().findOne(id)).map(dtoItem -> new ResponseEntity<>(dtoItem, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/loadAdvocates/all", method = GET, produces = APPLICATION_JSON_VALUE)
    public List<PersonDTO> findAll() throws URISyntaxException {
        return personDTOService.loadAllAdvocates();
    }

    @RequestMapping(value = "/loadAdvocates/{lawsuitId}/{courtSessionId}", method = GET, produces = APPLICATION_JSON_VALUE)
    public List<PersonDTO> findAllAdvocatesByLawsuitId(@PathVariable(value = "lawsuitId") Long lawsuitId, @PathVariable(value = "courtSessionId") Long courtSessionId) throws URISyntaxException {
        LocalDateTime courtSessionDate = courtSessionRepository.findById(courtSessionId).orElseThrow().getBeginDateTime();
        List<Person> all = attorneyRepository.getPersonByLawsuitId(lawsuitId, courtSessionDate.toLocalDate());
        return all.stream().map(each -> personDTOService.toDTO(each)).toList();
    }

    @RequestMapping(value = "/load/{direction}/{lawsuitId}", method = GET, produces = APPLICATION_JSON_VALUE)
    public List<PersonDTO> findAll(@PathVariable(value = "direction") Integer direction, @PathVariable(value = "lawsuitId") Long lawsuitId) throws URISyntaxException {
        var items = personRepository.getPersonByLawsuitIdAndDirection(lawsuitId, direction);
        if (DirectionType.OTV == DirectionType.parse(direction)) {
            items.addAll(personRepository.getPersonByLawsuitIdAndDirection(lawsuitId, (DirectionType.OTV_ADD.getId())));
        }
        if (DirectionType.ZAYAV == DirectionType.parse(direction)) {
            items.addAll(personRepository.getPersonByLawsuitIdAndDirection(lawsuitId, (DirectionType.ZAYAV_ADD.getId())));

        }
        List<PersonDTO> dtoItems = items.stream().map(personDTOService::toDTO).toList();
        return dtoItems;
    }

    @RequestMapping(value = "/find_client/{lawsuitId}", method = GET, produces = APPLICATION_JSON_VALUE)
    public PersonDTO findClient(@PathVariable(value = "lawsuitId") Long lawsuitId) throws URISyntaxException {
        Person client = personRepository.findClientByLawsuitId(lawsuitId);
        return personDTOService.toDTO(client);
    }

}
