package sud.domain.court;


import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sud.core.bc.filter.api.FilterExpression;
import sud.core.dto.support.PageRequestByFilter;
import sud.core.dto.support.PageResponse;
import sud.domain.person.PersonDTO;
import sud.domain.person.PersonDTOService;
import sud.domain.person.PersonRepository;
import sud.dto.CourtPersonLinkDTO;
import sud.domain.CourtPersonLink;
import sud.repositories.CourtPersonLinkRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourtDTOServiceImpl implements CourtDTOService {

    private final CourtRepository courtRepository;

    private final CourtPersonLinkRepository courtPersonLinkRepository;

    private final PersonDTOService personDTOService;
    private final PersonRepository personRepository;


    @Override
    public void deleteById(Long id) {
        courtRepository.deleteById(id);
    }

    @Override
    public CourtDTO save(CourtDTO dto) {
        if (dto == null) {
            return null;
        }
        final Court item;
        if (dto.isIdOldSet()) {
            Court courtTmp = courtRepository.findById(dto.getIdOld()).orElse(null);
            if (courtTmp != null) {
                item = courtTmp;
            } else {
                item = new Court();
                item.setId(dto.getIdOld());
            }
            CourtDTO oldValue = toDTO(item);

        } else {
            if (dto.getId() != null && courtRepository.findById(dto.getId()).orElse(null) != null)
                throw new ValidationException("Запись с данным ключом  " + dto.getId() + " уже существует");
            else
                item = new Court();
        }
        updateFields(item, dto);
        return toDTO(courtRepository.save(item));
    }

    public void updateFields(Court item, CourtDTO dto) {
        item.setId(dto.getId());
        item.setCourtType(dto.getCourtType());
        item.setShortName(dto.getShortName());
        item.setFullName(dto.getFullName());
        item.setAddress(dto.getAddress());
        item.setSite(dto.getSite());
        item.setTelNumber(dto.getTelNumber());
    }

    @Override
    public CourtDTO toDTO(Court court) {
        return toDTO(court, 1);
    }

    public CourtDTO toDTO(Court court, int depth) {
        return toDTO(court, depth, new CourtDTO());
    }

    public CourtDTO toDTO(Court item, int depth, CourtDTO dto) {
        if (item == null) {
            return null;
        }
        dto.setId(item.getId());
        dto.setIdOld(item.getId());
        dto.setCourtType(item.getCourtType());
        dto.setShortName(item.getShortName());
        dto.setFullName(item.getFullName());
        dto.setAddress(item.getAddress());
        dto.setSite(item.getSite());
        dto.setTelNumber(item.getTelNumber());
        return dto;
    }

    @Override
    public Court toEntity(CourtDTO dto) {
        return toEntity(dto, 1);
    }

    public Court toEntity(CourtDTO dto, int depth) {
        return toEntity(dto, depth, new Court());
    }

    public Court toEntity(CourtDTO dto, int depth, Court fromEntity) {
        if (dto == null) {
            return null;
        }
        updateFields(fromEntity, dto);
        return fromEntity;
    }

    @Override
    @Transactional
    public void saveLink(CourtPersonLinkDTO courtPersonLinkDTO) {
        PersonDTO courtPerson = personDTOService.save(courtPersonLinkDTO.getPerson());
        CourtPersonLink courtPersonLink = new CourtPersonLink();
        courtPersonLink.setCourt(courtRepository.getReferenceById(courtPersonLinkDTO.getCourtId()));
        courtPersonLink.setPerson(personRepository.getReferenceById(courtPerson.getId()));
        courtPersonLinkRepository.save(courtPersonLink);
    }

    @Override
    public PageResponse<CourtDTO> flexFilter(PageRequestByFilter<FilterExpression> var1) {
        System.out.println("-----------flexFilter");
        return null;
    }

    @Override
    public List<CourtDTO> search(String var1, Object var2, int var3) {
        System.out.println("-----------search");
        return List.of();
    }

    @Transactional(readOnly = true)
    @Override
    public List<CourtDTO> complete(String query, int maxResults, FilterExpression templateExpressions) {
        List<Court> results = courtRepository.findAll();
        return results.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public CourtDTO findOne(Long id) {
        CourtDTO dto = toDTO(courtRepository.findById(id).orElse(null));
        return dto;
    }
}
