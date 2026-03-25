package sud.domain.person;


import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sud.core.bc.filter.api.FilterExpression;
import sud.core.dto.support.PageRequestByFilter;
import sud.core.dto.support.PageResponse;
import sud.domain.LawsuitPersonId;
import sud.domain.LawsuitPersonLink;
import sud.domain.link.LawsuitPersonLinkRepository;
import sud.domain.user_data.UserDataService;
import sud.enums.DirectionType;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonDTOServiceImpl implements PersonDTOService {

    private final PersonRepository personRepository;
    private final UserDataService userDataService;
    private final LawsuitPersonLinkRepository lawsuitPersonLinkRepository;

    @Override
    public void deleteById(Long id) {
        personRepository.deleteById(id);
    }

    @Override
    public PersonDTO save(PersonDTO dto) {
        if (dto == null) {
            return null;
        }
        final Person item;
        if (dto.isIdOldSet()) {
            Person person = personRepository.findById(dto.getIdOld()).orElse(null);
            if (person != null) {
                item = person;
            } else {
                item = new Person();
                item.setId(dto.getIdOld());
            }
            PersonDTO oldValue = toDTO(item);

        } else {
            if (dto.getId() != null && personRepository.findById(dto.getId()).orElse(null) != null)
                throw new ValidationException("Запись с данным ключом  " + dto.getId() + " уже существует");
            else
                item = new Person();
        }
        updateFields(item, dto);
        PersonDTO createdPersonDTO = toDTO(personRepository.save(item));
        if (dto.getLawsuitId() != null) {
            LawsuitPersonLink link = new LawsuitPersonLink();
            link.setId(LawsuitPersonId.builder()
                    .lawsuitId(dto.getLawsuitId())
                    .personId(createdPersonDTO.getId())
                    .build());
            link.setClientFlag(dto.isNeedCreateUser());
            lawsuitPersonLinkRepository.save(link);
        }
        if (!dto.isNeedCreateUser()) {
            return createdPersonDTO;
        }
        userDataService.createUserData(createdPersonDTO.getEmail(), createdPersonDTO.getEmail(), createdPersonDTO.getId());
        return createdPersonDTO;
    }

    public void updateFields(Person item, PersonDTO dto) {
        item.setId(dto.getId());
        item.setDirection(dto.getDirection());
        item.setEmail(dto.getEmail());
        item.setBirthDate(dto.getBirthDate());
        item.setSurname(dto.getSurname());
        item.setFirstName(dto.getFirstName());
        item.setPatronymic(dto.getPatronymic());
        item.setName(dto.getName());
        item.setAddress(dto.getAddress());
        item.setPhone(dto.getPhone());
        item.setType(dto.getType());
    }

    @Override
    public PersonDTO toDTO(Person person) {
        return toDTO(person, 1);
    }

    public PersonDTO toDTO(Person person, int depth) {
        return toDTO(person, depth, new PersonDTO());
    }

    public PersonDTO toDTO(Person item, int depth, PersonDTO dto) {
        if (item == null) {
            return null;
        }
        dto.setId(item.getId());
        dto.setIdOld(item.getId());
        dto.setDirection(item.getDirection());
        dto.setEmail(item.getEmail());
        dto.setBirthDate(item.getBirthDate());
        dto.setSurname(item.getSurname());
        dto.setFirstName(item.getFirstName());
        dto.setPatronymic(item.getPatronymic());
        dto.setName(item.getName());
        dto.setAddress(item.getAddress());
        dto.setPhone(item.getPhone());
        dto.setType(item.getType());
        return dto;
    }

    @Override
    public Person toEntity(PersonDTO dto) {
        return toEntity(dto, 1);
    }

    public Person toEntity(PersonDTO dto, int depth) {
        return toEntity(dto, depth, new Person());
    }

    public Person toEntity(PersonDTO dto, int depth, Person fromEntity) {
        if (dto == null) {
            return null;
        }
        updateFields(fromEntity, dto);
        return fromEntity;
    }

    @Override
    public PageResponse<PersonDTO> flexFilter(PageRequestByFilter<FilterExpression> var1) {
        System.out.println("-----------flexFilter");
        return null;
    }

    @Override
    public List<PersonDTO> search(String var1, Object var2, int var3) {
        System.out.println("-----------search");
        return List.of();
    }

    @Transactional(readOnly = true)
    @Override
    public List<PersonDTO> complete(String query, int maxResults, FilterExpression templateExpressions) {
        List<Person> results = personRepository.findAll();
        return results.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public PersonDTO findOne(Long id) {
        PersonDTO dto = toDTO(personRepository.findById(id).orElse(null));
        return dto;
    }

    public List<PersonDTO> loadAllAdvocates() {
        List<Integer> directionIds = DirectionType.getAdvocatesGroup().stream().map(DirectionType::getId).toList();
        return personRepository.getAllByDirectionIn(directionIds).stream().map(this::toDTO).toList();
    }
}
