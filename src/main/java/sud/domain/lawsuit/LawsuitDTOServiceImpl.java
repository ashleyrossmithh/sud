package sud.domain.lawsuit;


import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sud.core.bc.filter.api.FilterExpression;
import sud.core.dto.support.PageRequestByFilter;
import sud.core.dto.support.PageResponse;
import sud.domain.LawsuitPersonId;
import sud.domain.LawsuitPersonLink;
import sud.domain.attorney.AttorneyRepository;
import sud.domain.history_status.HistoryStatusDTO;
import sud.domain.history_status.HistoryStatusDTOService;
import sud.domain.incoming_doc.IncomingDoc;
import sud.domain.incoming_doc.IncomingDocRepository;
import sud.domain.link.LawsuitPersonLinkRepository;
import sud.domain.person.Person;
import sud.domain.person.PersonDTO;
import sud.domain.person.PersonDTOService;
import sud.domain.person.PersonRepository;
import sud.domain.user_data.UserDataService;
import sud.dto.LawsuitRegistrDTO;
import sud.enums.DirectionType;
import sud.enums.HistoryStatusType;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LawsuitDTOServiceImpl implements LawsuitDTOService {

    private final LawsuitRepository lawsuitRepository;

    private final PersonDTOService personDTOService;

    private final PersonRepository personRepository;

    private final HistoryStatusDTOService historyStatusDTOService;

    private final IncomingDocRepository incomingDocRepository;

    private final AttorneyRepository attorneyRepository;

    private final UserDataService userDataService;

    private final LawsuitPersonLinkRepository lawsuitPersonLinkRepository;

    @Override
    public void deleteById(Long id) {
        lawsuitRepository.deleteById(id);
    }

    @Override
    public LawsuitDTO save(LawsuitDTO dto) {
        if (dto == null) {
            return null;
        }
        final Lawsuit item;
        if (dto.isIdOldSet()) {
            Lawsuit lawsuitTmp = lawsuitRepository.findById(dto.getIdOld()).orElse(null);
            if (lawsuitTmp != null) {
                item = lawsuitTmp;
            } else {
                item = new Lawsuit();
                item.setId(dto.getIdOld());
            }
            LawsuitDTO oldValue = toDTO(item);

        } else {
            if (dto.getId() != null && lawsuitRepository.findById(dto.getId()).orElse(null) != null)
                throw new ValidationException("Запись с данным ключом  " + dto.getId() + " уже существует");
            else
                item = new Lawsuit();
        }
        updateFields(item, dto);
        return toDTO(lawsuitRepository.save(item));
    }

    public void updateFields(Lawsuit item, LawsuitDTO dto) {
        item.setId(dto.getId());
        item.setCourtId(dto.getCourtId());
        item.setLsName(dto.getLsName());
        item.setLsDescription(dto.getLsDescription());
        item.setLsNumber(dto.getLsNumber());
        item.setRegNumber(dto.getRegNumber());
    }

    @Override
    public LawsuitDTO toDTO(Lawsuit lawsuit) {
        return toDTO(lawsuit, 1);
    }

    public LawsuitDTO toDTO(Lawsuit lawsuit, int depth) {
        return toDTO(lawsuit, depth, new LawsuitDTO());
    }

    public LawsuitDTO toDTO(Lawsuit item, int depth, LawsuitDTO dto) {
        if (item == null) {
            return null;
        }
        dto.setId(item.getId());
        dto.setIdOld(item.getId());
        dto.setCourtId(item.getCourtId());
        dto.setLsName(item.getLsName());
        dto.setLsDescription(item.getLsDescription());
        dto.setLsNumber(item.getLsNumber());
        dto.setRegNumber(item.getRegNumber());
        return dto;
    }

    @Override
    public Lawsuit toEntity(LawsuitDTO dto) {
        return toEntity(dto, 1);
    }

    public Lawsuit toEntity(LawsuitDTO dto, int depth) {
        return toEntity(dto, depth, new Lawsuit());
    }

    public Lawsuit toEntity(LawsuitDTO dto, int depth, Lawsuit fromEntity) {
        if (dto == null) {
            return null;
        }
        updateFields(fromEntity, dto);
        return fromEntity;
    }

    @Override
    @Transactional
    public void save(LawsuitRegistrDTO dto) {
        Lawsuit lawsuit = new Lawsuit();
        lawsuit.setCourtId(dto.getCourtId());
        lawsuit.setLsName(dto.getLawName());
        lawsuit.setLsNumber(dto.getLawNum());
        lawsuit.setLsDescription(dto.getLawDesc());
        lawsuitRepository.save(lawsuit);
        PersonDTO zayvPerson;
        boolean zayvClient = DirectionType.ZAYAV.getId().equals(dto.getClientType());
        if (zayvClient && dto.getZayav().getId() != null) {
            zayvPerson = personDTOService.findOne(dto.getZayav().getId());
            zayvPerson.setAddress(dto.getZayav().getAddress());
            zayvPerson.setBirthDate(dto.getZayav().getBirthDate());
            zayvPerson.setFirstName(dto.getZayav().getFirstName());
            zayvPerson.setSurname(dto.getZayav().getSurname());
            zayvPerson.setPatronymic(dto.getZayav().getPatronymic());
            zayvPerson.setName(dto.getZayav().getName());
            zayvPerson.setPhone(dto.getZayav().getPhone());
        } else {
            zayvPerson = dto.getZayav();
            zayvPerson.setId(null);
        }

        zayvPerson = personDTOService.save(zayvPerson);
        LawsuitPersonId id = new LawsuitPersonId(lawsuit.getId(),zayvPerson.getId());
        if (lawsuitPersonLinkRepository.findById(id).isEmpty()) {
            LawsuitPersonLink link = new LawsuitPersonLink();
            link.setId(id);
            link.setClientFlag(zayvClient);
            lawsuitPersonLinkRepository.save(link);
        }

        if (DirectionType.ZAYAV.getId() == dto.getClientType().intValue()) {
            if (dto.getBaseLawsuitId() == -1) {
                userDataService.createUserData(zayvPerson.getEmail(), zayvPerson.getId());
            }
        }
        if (dto.getCopyZayav() != null && dto.getCopyZayav()) {
            List<Person> zayavitelList = personRepository.loadAdditionPersonByDirection(dto.getBaseLawsuitId(), DirectionType.ZAYAV_ADD.getId());
            zayavitelList.forEach(cur -> {
                personRepository.save(cur);
                LawsuitPersonLink linkOld = lawsuitPersonLinkRepository.findById(LawsuitPersonId.builder().lawsuitId(dto.getBaseLawsuitId()).personId(cur.getId()).build()).orElse(null);
                if (linkOld == null) return;
                LawsuitPersonLink link = new LawsuitPersonLink();
                link.setId(LawsuitPersonId.builder().lawsuitId(lawsuit.getId()).personId(cur.getId()).build());
                link.setClientFlag(linkOld.getClientFlag());
                lawsuitPersonLinkRepository.save(link);
                lawsuitPersonLinkRepository.deleteById(linkOld.getId());
            });
        }
        if (dto.getCopyOtv() != null && dto.getCopyOtv()) {
            List<Person> otvList = personRepository.loadAdditionPersonByDirection(dto.getBaseLawsuitId(), DirectionType.OTV_ADD.getId());
            otvList.forEach(cur -> {
                personRepository.save(cur);
                LawsuitPersonLink linkOld = lawsuitPersonLinkRepository.findById(LawsuitPersonId.builder().lawsuitId(dto.getBaseLawsuitId()).personId(cur.getId()).build()).orElse(null);
                if (linkOld == null) return;
                LawsuitPersonLink link = new LawsuitPersonLink();
                link.setId(LawsuitPersonId.builder().lawsuitId(lawsuit.getId()).personId(cur.getId()).build());
                link.setClientFlag(linkOld.getClientFlag());
                lawsuitPersonLinkRepository.save(link);
                lawsuitPersonLinkRepository.deleteById(linkOld.getId());
            });
        }
        PersonDTO otvPerson;
        boolean otvClient = DirectionType.OTV.getId().equals(dto.getClientType());
        if (otvClient && dto.getOtv().getId() != null) {
            otvPerson = personDTOService.findOne(dto.getOtv().getId());
            otvPerson.setAddress(dto.getOtv().getAddress());
            otvPerson.setBirthDate(dto.getOtv().getBirthDate());
            otvPerson.setFirstName(dto.getOtv().getFirstName());
            otvPerson.setSurname(dto.getOtv().getSurname());
            otvPerson.setPatronymic(dto.getOtv().getPatronymic());
            otvPerson.setName(dto.getOtv().getName());
            otvPerson.setPhone(dto.getOtv().getPhone());
        } else {
            otvPerson = dto.getOtv();
            otvPerson.setId(null);
        }

        otvPerson = personDTOService.save(otvPerson);
        id = new LawsuitPersonId(lawsuit.getId(),otvPerson.getId());
        if (lawsuitPersonLinkRepository.findById(id).isEmpty()) {
            LawsuitPersonLink otvLink = new LawsuitPersonLink();
            otvLink.setId(id);
            otvLink.setClientFlag(otvClient);
            lawsuitPersonLinkRepository.save(otvLink);
        }

        if (DirectionType.OTV.getId() ==  dto.getClientType().intValue()) {
            if (dto.getBaseLawsuitId() == -1) {
                userDataService.createUserData(otvPerson.getEmail(), otvPerson.getId());
            }
        }
        HistoryStatusDTO historyStatusDTO = new HistoryStatusDTO();
        historyStatusDTO.setLawsuitId(lawsuit.getId());
        historyStatusDTO.setCode(HistoryStatusType.CREATED.getId());
        historyStatusDTO.setStartDate(LocalDateTime.now());
        if (dto.getBaseLawsuitId() != -1) {
/*            List<IncomingDoc> docs = incomingDocRepository.findByLawsuit(lawsuit);
            docs.forEach(cur -> {
                cur.setLawsuit(lawsuit);
                incomingDocRepository.save(cur);
            });*/
            attorneyRepository.getAllByLawsuitId(lawsuit.getId()).forEach(attorney -> {
                attorney.setLawsuit(lawsuit);
                attorneyRepository.save(attorney);
            });
            HistoryStatusDTO historyCloseStatusDTO = new HistoryStatusDTO();
            historyCloseStatusDTO.setLawsuitId(dto.getBaseLawsuitId());
            historyCloseStatusDTO.setCode(HistoryStatusType.FINISHED.getId());
            historyCloseStatusDTO.setStartDate(LocalDateTime.now());
            historyStatusDTOService.save(historyCloseStatusDTO);
        }
        this.historyStatusDTOService.save(historyStatusDTO);
    }

    @Override
    public PageResponse<LawsuitDTO> flexFilter(PageRequestByFilter<FilterExpression> var1) {
        System.out.println("-----------flexFilter");
        return null;
    }

    @Override
    public List<LawsuitDTO> search(String var1, Object var2, int var3) {
        System.out.println("-----------search");
        return List.of();
    }

    @Transactional(readOnly = true)
    @Override
    public List<LawsuitDTO> complete(String query, int maxResults, FilterExpression templateExpressions) {
        List<Lawsuit> results = lawsuitRepository.findAll();
        return results.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public LawsuitDTO findOne(Long id) {
        LawsuitDTO dto = toDTO(lawsuitRepository.findById(id).orElse(null));
        return dto;
    }
}
