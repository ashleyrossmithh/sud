package sud.rest.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sud.core.action.checkutils.Check;
import sud.domain.history_status.HistoryStatusDTO;
import sud.domain.history_status.HistoryStatusDTOService;
import sud.domain.history_status.HistoryStatusRepository;
import sud.domain.lawsuit.LawsuitRepository;
import sud.domain.person.Person;
import sud.domain.person.PersonDTO;
import sud.domain.person.PersonDTOService;
import sud.domain.person.PersonRepository;
import sud.enums.DirectionType;
import sud.enums.HistoryStatusType;
import sud.enums.SelectItemImpl;
import sud.services.MailService;

import java.net.URISyntaxException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HistoryStatusResource {
    private final HistoryStatusDTOService historyStatusDTOService;
    private final MailService mailService;
    private final PersonRepository personRepository;
    private final LawsuitRepository lawsuitRepository;
    private final HistoryStatusRepository historyStatusRepository;
    private final PersonDTOService personDTOService;

    @RequestMapping(
            value = {"/historyStatus/create"},
            method = {RequestMethod.POST},
            produces = {"application/json"}
    )
    public ResponseEntity<Check> create(@RequestBody HistoryStatusDTO historyStatusDTO) throws URISyntaxException {
        if (HistoryStatusType.WAIT_REPEAT == HistoryStatusType.parseByCode(historyStatusDTO.getCode())) {
            var lastStatus = historyStatusRepository.getLastHistoryStatus(historyStatusDTO.getLawsuitId());
            if (historyStatusDTO.getStartDate().isBefore(lastStatus.getStartDateTime())) {
                return ResponseEntity.ok().body(Check.error("Повторное заседание не может быть назначено ранее " + lastStatus.getStartDateTime().format(DateTimeFormatter.ISO_DATE_TIME)));
            }
        }
        historyStatusDTOService.save(historyStatusDTO);
        var check = sendEmail(historyStatusDTO.getLawsuitId(), historyStatusDTO.getName());
        return ResponseEntity.ok().body(check);
    }

    @GetMapping(value = "/historyStatusType/all/{curStatusCode}", produces = APPLICATION_JSON_VALUE)
    public List<SelectItemImpl<Integer>> getHistoryTypes(@PathVariable Integer curStatusCode) {
        return HistoryStatusType.asSelectItems(curStatusCode);
    }

    @RequestMapping(value = "/historyStatus/{lawsuitId}", method = GET, produces = APPLICATION_JSON_VALUE)
    public List<HistoryStatusDTO> getAllByLawsuitId(@PathVariable Long lawsuitId) throws URISyntaxException {
        return historyStatusDTOService.getAllByLawsuitId(lawsuitId);
    }

    @RequestMapping(value = "/historyStatus/load/{historyStatusId}", method = GET, produces = APPLICATION_JSON_VALUE)
    public List<PersonDTO> getAllPersonByHistoryStatusId(@PathVariable Long historyStatusId) throws URISyntaxException {
        List<Person> items = personRepository.loadPersonByHistoryStatusId(historyStatusId);
        return items.stream().map(personDTOService::toDTO).toList();
    }

    private Check sendEmail(Long lawsuitId, String newStatus) {
        List<Person> persons = personRepository.getPersonByLawsuitIdAndDirection(lawsuitId, DirectionType.ZAYAV.getId());//todo нужно определиь кто клиент заявитель или ответчик
        if (persons != null && !persons.isEmpty() && persons.stream().findFirst().orElseThrow().getEmail() != null) {
            try {
                if (true)
                    throw new RuntimeException("пока отключена отправка почты");//todo пока отключена отправка почты
                // нужно разобратся с авторизацией на yandex
                mailService.sendSimpleEmail(persons.stream().findFirst().orElseThrow().getEmail(), newStatus);
            } catch (Exception ex) {
                return Check.criticalError("Ошибка при отправке сообщения об изменении статуса. " + ex.getMessage());
            }
            return Check.info("Сообщение об изменении статуса отправлено на электронную почту: " + persons.stream().findFirst().orElseThrow().getEmail());
        } else {
            return Check.error("Сообщение об изменении статуса не было отправлено т.к. не найден email заявителя");
        }
    }
}
