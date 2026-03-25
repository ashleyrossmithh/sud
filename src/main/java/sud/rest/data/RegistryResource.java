package sud.rest.data;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sud.domain.lawsuit.Lawsuit;
import sud.domain.person.Person;
import sud.domain.person.PersonRepository;
import sud.enums.DirectionType;
import sud.enums.HistoryStatusType;
import sud.registr.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RegistryResource {

    private final RegistrService registrService;
    private final PersonRepository personRepository;

    @GetMapping(value = "/lawsuitRegistrRec/all", produces = APPLICATION_JSON_VALUE)
    public List<LawsuitRegistrRec> findAllLawsuit() {
        List<LawsuitRegistrRec> allLawsuit = registrService.findAllLawsuit();
        allLawsuit.forEach(e -> e.setStatusDesc(HistoryStatusType.parseByCode(e.getStatusCode()).getName()));
        return allLawsuit;
    }

    @GetMapping(value = "/lawsuitRegistrRec/person/{personId}", produces = APPLICATION_JSON_VALUE)
    public List<LawsuitRegistrRec> findAllLawsuitBuPersonId(@PathVariable(value = "personId") Long personId) {
        Person person = personRepository.findById(personId).orElseThrow();
        List<LawsuitRegistrRec> allLawsuit;
        if (DirectionType.parse(person.getDirection()).isAdvocateGroup()) {
            allLawsuit = registrService.findAllLawsuit(null);
        } else {
            allLawsuit = new ArrayList<>();
            List<String> items = personRepository.findLawsuitByPersonId(personId).stream().map(Lawsuit::getLsNumber).toList();
            new HashSet<>(items).forEach(lsNumber -> allLawsuit.addAll(registrService.findAllLawsuit(lsNumber)));
        }
        allLawsuit.forEach(e -> e.setStatusDesc(HistoryStatusType.parseByCode(e.getStatusCode()).getName()));
        return allLawsuit;
    }

    @GetMapping(value = "/courtSessionRegistrRec/all", produces = APPLICATION_JSON_VALUE)
    public List<CourtSessionRegistrRec> findAllCourtSessions() {
        List<CourtSessionRegistrRec> items = registrService.findAllCourtSession(null);
        items.forEach(e -> e.setStatusDesc(HistoryStatusType.parseByCode(e.getStatusCode()).getName()));
        return items;
    }

    @GetMapping(value = "/courtSessionRegistrRec/{personId}", produces = APPLICATION_JSON_VALUE)
    public List<CourtSessionRegistrRec> findCourtSessionsByPersonId(@PathVariable(value = "personId") Long personId) {
        List<CourtSessionRegistrRec> items = registrService.findAllCourtSession(personId);
        items = items.stream().filter(cur -> cur.getAdvocatCount() > 0).toList();
        items.forEach(e -> e.setStatusDesc(HistoryStatusType.parseByCode(e.getStatusCode()).getName()));
        return items;
    }

    @GetMapping(value = "/preparedLawsuitRegistrRec/all", produces = APPLICATION_JSON_VALUE)
    public List<PreparedLawsuitRegistrRec> findPreparedLawsuitAll() {
        List<PreparedLawsuitRegistrRec> items = registrService.findAllHistoryStatusLawsuit(null);
        items.forEach(e -> e.setStatusDesc(HistoryStatusType.parseByCode(e.getCode()).getName()));
        return items;
    }

    @GetMapping(value = "/preparedLawsuitRegistrRec/{personId}", produces = APPLICATION_JSON_VALUE)
    public List<PreparedLawsuitRegistrRec> findPreparedLawsuitAllByPersonId(@PathVariable(value = "personId") Long personId) {
        List<PreparedLawsuitRegistrRec> items = registrService.findAllHistoryStatusLawsuit(personId);
        items = items.stream().filter(cur -> cur.getResponseCount() > 0).toList();
        items.forEach(e -> e.setStatusDesc(HistoryStatusType.parseByCode(e.getCode()).getName()));
        return items;
    }

    @GetMapping(value = "/incomingDocRegistrRec/{lawsuitId}", produces = APPLICATION_JSON_VALUE)
    public List<IncomingDocRegistrRec> findAllIncomingDocs(@PathVariable(value = "lawsuitId") Long lawsuitId) {
        return registrService.findIncomingDocsByLawsuitId(lawsuitId);
    }

    @GetMapping(value = "/incomingDocRegistrRec/history-status/{historyStatusId}", produces = APPLICATION_JSON_VALUE)
    public List<IncomingDocRegistrRec> findByHistoryStatusIncomingDocs(@PathVariable(value = "historyStatusId") Long historyStatusId) {
        return registrService.findIncomingDocsByHistoryStatusId(historyStatusId);
    }
}
