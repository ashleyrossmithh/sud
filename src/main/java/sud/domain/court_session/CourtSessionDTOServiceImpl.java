package sud.domain.court_session;


import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sud.core.bc.filter.api.FilterExpression;
import sud.core.dto.support.PageRequestByFilter;
import sud.core.dto.support.PageResponse;
import sud.domain.lawsuit.LawsuitRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourtSessionDTOServiceImpl implements CourtSessionDTOService {

    private final CourtSessionRepository courtSessionRepository;
    private final LawsuitRepository lawsuitRepository;


    @Override
    public void deleteById(Long id) {
        courtSessionRepository.deleteById(id);
    }

    @Override
    public CourtSessionDTO save(CourtSessionDTO dto) {
        if (dto == null) {
            return null;
        }
        final CourtSession item;
        if (dto.isIdOldSet()) {
            CourtSession courtSessionTmp = courtSessionRepository.findById(dto.getIdOld()).orElse(null);
            if (courtSessionTmp != null) {
                item = courtSessionTmp;
            } else {
                item = new CourtSession();
                item.setId(dto.getIdOld());
            }
            CourtSessionDTO oldValue = toDTO(item);

        } else {
            if (dto.getId() != null && courtSessionRepository.findById(dto.getId()).orElse(null) != null)
                throw new ValidationException("Запись с данным ключом  " + dto.getId() + " уже существует");
            else
                item = new CourtSession();
        }
        updateFields(item, dto);
        return toDTO(courtSessionRepository.save(item));
    }

    public void updateFields(CourtSession item, CourtSessionDTO dto) {
        item.setId(dto.getId());
        item.setLawsuit(lawsuitRepository.getReferenceById(dto.getLawsuitId()));
        item.setResult(dto.getResult());
        item.setNote(dto.getNote());
        item.setBeginDateTime(dto.getBeginDateTime());
        item.setNeedPresence(dto.getNeedPresence());
    }

    @Override
    public CourtSessionDTO toDTO(CourtSession courtSession) {
        return toDTO(courtSession, 1);
    }

    public CourtSessionDTO toDTO(CourtSession courtSession, int depth) {
        return toDTO(courtSession, depth, new CourtSessionDTO());
    }

    public CourtSessionDTO toDTO(CourtSession item, int depth, CourtSessionDTO dto) {
        if (item == null) {
            return null;
        }
        dto.setId(item.getId());
        dto.setIdOld(item.getId());
        dto.setLawsuitId(item.getLawsuit().getId());
        dto.setResult(item.getResult());
        dto.setNote(item.getNote());
        dto.setBeginDateTime(item.getBeginDateTime());
        dto.setNeedPresence(item.getNeedPresence());
        return dto;
    }

    @Override
    public CourtSession toEntity(CourtSessionDTO dto) {
        return toEntity(dto, 1);
    }

    public CourtSession toEntity(CourtSessionDTO dto, int depth) {
        return toEntity(dto, depth, new CourtSession());
    }

    public CourtSession toEntity(CourtSessionDTO dto, int depth, CourtSession fromEntity) {
        if (dto == null) {
            return null;
        }
        updateFields(fromEntity, dto);
        return fromEntity;
    }

    @Override
    public List<CourtSessionDTO> getAllByLawsuitId(Long lawsuitId) {
        return courtSessionRepository.getAllByLawsuitId(lawsuitId).stream().map(this::toDTO).toList();
    }

    @Override
    public PageResponse<CourtSessionDTO> flexFilter(PageRequestByFilter<FilterExpression> var1) {
        System.out.println("-----------flexFilter");
        return null;
    }

    @Override
    public List<CourtSessionDTO> search(String var1, Object var2, int var3) {
        System.out.println("-----------search");
        return List.of();
    }

    @Transactional(readOnly = true)
    @Override
    public List<CourtSessionDTO> complete(String query, int maxResults, FilterExpression templateExpressions) {
        List<CourtSession> results = courtSessionRepository.findAll();
        return results.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public CourtSessionDTO findOne(Long id) {
        CourtSessionDTO dto = toDTO(courtSessionRepository.findById(id).orElse(null));
        return dto;
    }
}
