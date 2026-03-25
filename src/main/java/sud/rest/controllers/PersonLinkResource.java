package sud.rest.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import sud.domain.court_session.CourtSession;
import sud.domain.court_session.CourtSessionRepository;
import sud.domain.person.PersonDTO;
import sud.domain.person.PersonDTOService;
import sud.domain.person.PersonRepository;
import sud.dto.PersonWithLinkDTO;
import sud.dto.PersonWithParentDto;
import sud.repositories.CourtSessionPersonLinkRepository;
import sud.services.PersonLinkService;

import java.net.URISyntaxException;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PersonLinkResource {
    private final PersonLinkService personLinkService;
    private final PersonDTOService personDTOService;
    private final PersonRepository personRepository;
    private final CourtSessionRepository courtSessionRepository;
    private final CourtSessionPersonLinkRepository courtSessionPersonLinkRepository;

    @RequestMapping(
            value = {"/courtPerson/create"},
            method = {RequestMethod.POST},
            produces = {"application/json"}
    )
    public ResponseEntity<Void> createNew(@RequestBody PersonWithParentDto personWithParentDto) throws URISyntaxException {
        personLinkService.createCourtPersonLink(personWithParentDto.getPersonDTO(), personWithParentDto.getParentId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @RequestMapping(value = "/loadPersonByCourtId/{courtId}", method = GET, produces = APPLICATION_JSON_VALUE)
    public List<PersonDTO> findById(@PathVariable Long courtId) throws URISyntaxException {
        return personRepository.loadCourtPersonByCourtId(courtId).stream().map(personDTOService::toDTO).toList();
    }

    @RequestMapping(
            value = {"/courtSessionPersonLink/create"},
            method = {RequestMethod.POST},
            produces = {"application/json"}
    )
    @Transactional
    public ResponseEntity<Void> createCourtSessionLink(@RequestBody PersonWithLinkDTO personWithLink) throws URISyntaxException {
        courtSessionPersonLinkRepository.deleteAll(courtSessionPersonLinkRepository.findAllByCourtSession_Id(personWithLink.getCourtSessionId()));
        personLinkService.createCourtSessionPersonLink(personWithLink.getPersonIds(), personWithLink.getCourtSessionId());
        CourtSession courtSession = courtSessionRepository.findById(personWithLink.getCourtSessionId()).orElseThrow();
        courtSession.setNeedPresence(personWithLink.getNeedPresence() != null && personWithLink.getNeedPresence());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
