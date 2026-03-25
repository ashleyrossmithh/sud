package sud.domain.attorney;


import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sud.core.bc.filter.api.FilterExpression;
import sud.core.dto.support.PageRequestByFilter;
import sud.core.dto.support.PageResponse;
import sud.domain.lawsuit.LawsuitRepository;
import sud.domain.person.PersonRepository;
import sud.dto.AttorneyToPersonsDTO;
import sud.domain.AttorneyPersonLink;
import sud.repositories.AttorneyPersonLinkRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttorneyDTOServiceImpl implements AttorneyDTOService {

    private final AttorneyRepository attorneyRepository;
    private final LawsuitRepository lawsuitRepository;
    private final PersonRepository personRepository;
    private final AttorneyPersonLinkRepository attorneyPersonLinkRepository;

    @Override
    public void deleteById(Long id) {
        attorneyRepository.deleteById(id);
    }

    @Override
    public AttorneyDTO save(AttorneyDTO dto) {
        if (dto == null) {
            return null;
        }
        final Attorney item;
        if (dto.isIdOldSet()) {
            Attorney attorneyTmp = attorneyRepository.findById(dto.getIdOld()).orElse(null);
            if (attorneyTmp != null) {
                item = attorneyTmp;
            } else {
                item = new Attorney();
                item.setId(dto.getIdOld());
            }
            AttorneyDTO oldValue = toDTO(item);

        } else {
            if (dto.getId() != null && attorneyRepository.findById(dto.getId()).orElse(null) != null)
                throw new ValidationException("Запись с данным ключом  " + dto.getId() + " уже существует");
            else
                item = new Attorney();
        }
        updateFields(item, dto);
        return toDTO(attorneyRepository.save(item));
    }

    public void updateFields(Attorney item, AttorneyDTO dto) {
        item.setId(dto.getId());
        item.setLawsuit(lawsuitRepository.getReferenceById(dto.getLawsuitId()));
        item.setFromPersonId(dto.getFromPersonId());
        item.setNumber(dto.getNumber());
        item.setBeginDate(dto.getBeginDate());
        item.setEndDate(dto.getEndDate());
        item.setDescription(dto.getDescription());
    }

    @Override
    public AttorneyDTO toDTO(Attorney attorney) {
        return toDTO(attorney, 1);
    }

    public AttorneyDTO toDTO(Attorney attorney, int depth) {
        return toDTO(attorney, depth, new AttorneyDTO());
    }

    public AttorneyDTO toDTO(Attorney item, int depth, AttorneyDTO dto) {
        if (item == null) {
            return null;
        }
        dto.setId(item.getId());
        dto.setIdOld(item.getId());
        dto.setLawsuitId(item.getLawsuit().getId());
        dto.setFromPersonId(item.getFromPersonId());
        dto.setNumber(item.getNumber());
        dto.setBeginDate(item.getBeginDate());
        dto.setEndDate(item.getEndDate());
        dto.setDescription(item.getDescription());
        if (item.getEndDate() != null) {
            dto.setActual(LocalDate.now().isBefore(item.getEndDate()));
        }
        return dto;
    }

    @Override
    public Attorney toEntity(AttorneyDTO dto) {
        return toEntity(dto, 1);
    }

    public Attorney toEntity(AttorneyDTO dto, int depth) {
        return toEntity(dto, depth, new Attorney());
    }

    public Attorney toEntity(AttorneyDTO dto, int depth, Attorney fromEntity) {
        if (dto == null) {
            return null;
        }
        updateFields(fromEntity, dto);
        return fromEntity;
    }

    @Override
    @Transactional
    public void save(AttorneyToPersonsDTO attorneyToPersonsDTO) {
        Attorney attorney = attorneyRepository.save(toEntity(attorneyToPersonsDTO.getAttorney()));
        attorneyToPersonsDTO.getPersonIdList().forEach(personId -> {
            AttorneyPersonLink attorneyPersonLink = new AttorneyPersonLink();
            attorneyPersonLink.setPerson(personRepository.getReferenceById(personId));
            attorneyPersonLink.setAttorney(attorney);
            attorneyPersonLinkRepository.save(attorneyPersonLink);
        });
    }

    @Override
    public List<AttorneyDTO> getAllByLawsuitId(Long lawsuitId) {
        return attorneyRepository.getAllByLawsuitId(lawsuitId).stream().map(this::toDTO).toList();
    }

    @Override
    public PageResponse<AttorneyDTO> flexFilter(PageRequestByFilter<FilterExpression> var1) {
        System.out.println("-----------flexFilter");
        return null;
    }

    @Override
    public List<AttorneyDTO> search(String var1, Object var2, int var3) {
        System.out.println("-----------search");
        return List.of();
    }

    @Transactional(readOnly = true)
    @Override
    public List<AttorneyDTO> complete(String query, int maxResults, FilterExpression templateExpressions) {
        List<Attorney> results = attorneyRepository.findAll();
        return results.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public AttorneyDTO findOne(Long id) {
        AttorneyDTO dto = toDTO(attorneyRepository.findById(id).orElse(null));
        return dto;
    }
}
