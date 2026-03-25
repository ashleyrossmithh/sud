package sud.domain.incoming_doc;


import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sud.core.bc.filter.api.FilterExpression;
import sud.core.dto.support.PageRequestByFilter;
import sud.core.dto.support.PageResponse;
import sud.domain.history_status.HistoryStatusRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomingDocDTOServiceImpl implements IncomingDocDTOService {

    private final IncomingDocRepository incomingDocRepository;
    private final HistoryStatusRepository historyStatusRepository;

    @Override
    public void deleteById(Long id) {
        incomingDocRepository.deleteById(id);
    }

    @Override
    public IncomingDocDTO save(IncomingDocDTO dto) {
        if (dto == null) {
            return null;
        }
        final IncomingDoc item;
        if (dto.isIdOldSet()) {
            IncomingDoc incomingDocTmp = incomingDocRepository.findById(dto.getIdOld()).orElse(null);
            if (incomingDocTmp != null) {
                item = incomingDocTmp;
            } else {
                item = new IncomingDoc();
                item.setId(dto.getIdOld());
            }
            IncomingDocDTO oldValue = toDTO(item);

        } else {
            if (dto.getId() != null && incomingDocRepository.findById(dto.getId()).orElse(null) != null)
                throw new ValidationException("Запись с данным ключом  " + dto.getId() + " уже существует");
            else
                item = new IncomingDoc();
        }
        updateFields(item, dto);
        return toDTO(incomingDocRepository.save(item));
    }

    public void updateFields(IncomingDoc item, IncomingDocDTO dto) {
        item.setId(dto.getId());
        item.setHistoryStatus(this.historyStatusRepository.getReferenceById(dto.getHistoryStatusId()));
        item.setIdCategory(dto.getIdCategory());
        item.setIdAuthor(dto.getIdAuthor());
        item.setIdDocDate(dto.getIdDocDate());
        item.setIdDocNum(dto.getIdDocNum());
        item.setIdName(dto.getIdName());
        item.setIdNote(dto.getIdNote());
        item.setIdCategory(dto.getIdCategory());
        item.setHasOriginal(dto.getHasOriginal());
    }

    @Override
    public IncomingDocDTO toDTO(IncomingDoc incomingDoc) {
        return toDTO(incomingDoc, 1);
    }

    public IncomingDocDTO toDTO(IncomingDoc incomingDoc, int depth) {
        return toDTO(incomingDoc, depth, new IncomingDocDTO());
    }

    public IncomingDocDTO toDTO(IncomingDoc item, int depth, IncomingDocDTO dto) {
        if (item == null) {
            return null;
        }
        dto.setId(item.getId());
        dto.setIdOld(item.getId());
        dto.setHistoryStatusId(item.getHistoryStatus().getId());
        dto.setIdCategory(item.getIdCategory());
        dto.setIdAuthor(item.getIdAuthor());
        dto.setIdDocDate(item.getIdDocDate());
        dto.setIdDocNum(item.getIdDocNum());
        dto.setIdName(item.getIdName());
        dto.setIdNote(item.getIdNote());
        dto.setIdCategory(item.getIdCategory());
        dto.setHasOriginal(item.getHasOriginal());
        return dto;
    }

    @Override
    public IncomingDoc toEntity(IncomingDocDTO dto) {
        return toEntity(dto, 1);
    }

    public IncomingDoc toEntity(IncomingDocDTO dto, int depth) {
        return toEntity(dto, depth, new IncomingDoc());
    }

    public IncomingDoc toEntity(IncomingDocDTO dto, int depth, IncomingDoc fromEntity) {
        if (dto == null) {
            return null;
        }
        updateFields(fromEntity, dto);
        return fromEntity;
    }

    @Override
    public PageResponse<IncomingDocDTO> flexFilter(PageRequestByFilter<FilterExpression> var1) {
        System.out.println("-----------flexFilter");
        return null;
    }

    @Override
    public List<IncomingDocDTO> search(String var1, Object var2, int var3) {
        System.out.println("-----------search");
        return List.of();
    }

    @Transactional(readOnly = true)
    @Override
    public List<IncomingDocDTO> complete(String query, int maxResults, FilterExpression templateExpressions) {
        List<IncomingDoc> results = incomingDocRepository.findAll();
        return results.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public IncomingDocDTO findOne(Long id) {
        IncomingDocDTO dto = toDTO(incomingDocRepository.findById(id).orElse(null));
        return dto;
    }
}
