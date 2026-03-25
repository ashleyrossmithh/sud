package sud.domain.history_status;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sud.core.bc.filter.api.FilterExpression;
import sud.core.dto.support.PageRequestByFilter;
import sud.core.dto.support.PageResponse;
import sud.domain.CourtPersonLawsuitLink;
import sud.domain.HistoryStatusPersonId;
import sud.domain.HistoryStatusPersonLink;
import sud.domain.court_session.CourtSession;
import sud.domain.court_session.CourtSessionRepository;
import sud.domain.lawsuit.Lawsuit;
import sud.domain.lawsuit.LawsuitRepository;
import sud.domain.link.HistoryStatusPersonLinkRepository;
import sud.enums.HistoryStatusType;
import sud.repositories.CourtPersonLawsuitLinkRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryStatusDTOServiceImpl implements HistoryStatusDTOService {

    private final HistoryStatusRepository historyStatusRepository;
    private final CourtSessionRepository courtSessionRepository;
    private final LawsuitRepository lawsuitRepository;
    private final CourtPersonLawsuitLinkRepository courtPersonLawsuitLinkRepository;
    private final HistoryStatusPersonLinkRepository historyStatusPersonLinkRepository;

    @Override
    @Transactional(readOnly = true)
    public HistoryStatusDTO findOne(Long id) {
        return toDTO(historyStatusRepository.findById(id).orElse(null));
    }

    @Override
    public void updateFields(HistoryStatus item, HistoryStatusDTO dto) {
        item.setId(dto.getId());
        item.setLawsuit(lawsuitRepository.getReferenceById(dto.getLawsuitId()));
        item.setStartDateTime(dto.getStartDate());
        item.setCode(dto.getCode());
        item.setNote(dto.getNote());
    }

    @Override
    public void deleteById(Long id) {
        historyStatusRepository.deleteById(id);
    }

    @Override
    public HistoryStatusDTO save(HistoryStatusDTO dto) {
        if (dto == null) {
            return null;
        }
        if (dto.getTime() == null || dto.getTime().isEmpty()) {
            dto.setStartDate(LocalDateTime.of(dto.getStartDate().toLocalDate(), LocalTime.now()));
        } else {
            dto.setStartDate(LocalDateTime.of(dto.getStartDate().toLocalDate(), LocalTime.parse(dto.getTime(), DateTimeFormatter.ISO_LOCAL_TIME)));
        }
        final HistoryStatus item;
        if (dto.isIdOldSet()) {
            HistoryStatus attorneyTmp = historyStatusRepository.findById(dto.getIdOld()).orElse(null);
            if (attorneyTmp != null) {
                item = attorneyTmp;
            } else {
                item = new HistoryStatus();
                item.setId(dto.getIdOld());
            }
            HistoryStatusDTO oldValue = toDTO(item);

        } else {
            if (dto.getId() != null && historyStatusRepository.findById(dto.getId()).orElse(null) != null)
                throw new ValidationException("Запись с данным ключом  " + dto.getId() + " уже существует");
            else
                item = new HistoryStatus();
        }
        updateFields(item, dto);
        if (HistoryStatusType.parseByCode(dto.getCode()).equals(HistoryStatusType.WAIT)
                || HistoryStatusType.parseByCode(dto.getCode()).equals(HistoryStatusType.WAIT_REPEAT)) {
            CourtSession courtSession = new CourtSession();
            courtSession.setLawsuit(this.lawsuitRepository.getReferenceById(dto.getLawsuitId()));
            courtSession.setBeginDateTime(dto.getStartDate());
            courtSession.setNote("Создано автоматически при смене статуса");
            courtSessionRepository.save(courtSession);
        }
        if (HistoryStatusType.parseByCode(dto.getCode()).equals(HistoryStatusType.REGISTRED)) {
            Lawsuit lawsuit = lawsuitRepository.findById(dto.getLawsuitId()).orElseThrow();
            lawsuit.setRegNumber(dto.getLsRegNum());
            lawsuitRepository.save(lawsuit);
        }
        if (HistoryStatusType.parseByCode(dto.getCode()).equals(HistoryStatusType.STARTED)) {
            CourtPersonLawsuitLink link = new CourtPersonLawsuitLink();
            link.setCourt_person_link_id(dto.getPersonId());
            Lawsuit lawsuit = lawsuitRepository.getReferenceById(dto.getLawsuitId());
            link.setLawsuit(lawsuit);
            courtPersonLawsuitLinkRepository.save(link);
        }
        HistoryStatusDTO saved = toDTO(historyStatusRepository.save(item));
        if (dto.getResponseblePersonIds() != null) {
            dto.getResponseblePersonIds().forEach(cur -> {
                HistoryStatusPersonLink link = new HistoryStatusPersonLink();
                link.setId(HistoryStatusPersonId.builder().historyStatusId(saved.getId()).personId(cur).build());
                historyStatusPersonLinkRepository.save(link);
            });
        }
        return saved;
    }

    @Override
    public HistoryStatusDTO toDTO(HistoryStatus item) {
        return toDTO(item, 1);
    }

    public HistoryStatusDTO toDTO(HistoryStatus item, int depth, HistoryStatusDTO dto) {
        if (item == null) {
            return null;
        }
        dto.setId(item.getId());
        dto.setIdOld(item.getId());
        dto.setLawsuitId(item.getLawsuit().getId());
        dto.setStartDate(item.getStartDateTime());
        dto.setCode(item.getCode());
        dto.setNote(item.getNote());
        dto.setName(HistoryStatusType.parseByCode(item.getCode()).getName());
        return dto;
    }

    public HistoryStatusDTO toDTO(HistoryStatus item, int depth) {
        return toDTO(item, depth, new HistoryStatusDTO());
    }

    @Override
    public PageResponse<HistoryStatusDTO> flexFilter(PageRequestByFilter<FilterExpression> var1) {
        System.out.println("-----------flexFilter");
        return null;
    }

    @Override
    public List<HistoryStatusDTO> search(String var1, Object var2, int var3) {
        System.out.println("-----------search");
        return List.of();
    }

    @Override
    public HistoryStatus toEntity(HistoryStatusDTO dto, int depth, HistoryStatus fromEntity) {
        if (dto == null) {
            return null;
        }
        updateFields(fromEntity, dto);
        return fromEntity;
    }

    @Override
    public List<HistoryStatusDTO> getAllByLawsuitId(Long lawsuitId) {
        return historyStatusRepository.getAllByLawsuitId(lawsuitId).stream().map(this::toDTO).toList();
    }
}
