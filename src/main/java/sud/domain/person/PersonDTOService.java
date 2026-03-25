package sud.domain.person;

import sud.core.dto.support.IUpdatableBaseDTOService;

import java.util.List;

public interface PersonDTOService extends IUpdatableBaseDTOService<PersonDTO, Person, PersonRepository, Long> {


 //   CourtDTO createNew();

    PersonDTO findOne(Long id);

    void updateFields(Person item, PersonDTO dto);

    PersonDTO save(PersonDTO dto);

    /**
     * Converts the passed Person to a DTO.
     */
    PersonDTO toDTO(Person item);

    /**
     * Converts the passed Person to a DTO.
     */
    default PersonDTO toDTO(Person item, int depth) {
        return toDTO(item, depth, new PersonDTO());
    }


    PersonDTO toDTO(Person item, int depth, PersonDTO fromDTO);

    /**
     * Converts the passed dto to a Person.
     * Convenient for query by example.
     */
    default Person toEntity(PersonDTO dto) {
        return toEntity(dto, 1);
    }

    /**
     * Converts the passed dto to a Person.
     * Convenient for query by example.
     */
    default Person toEntity(PersonDTO dto, int depth) {
        return toEntity(dto, depth, new Person());
    }

    /**
     * Converts the passed dto to a Person.
     * Convenient for query by example.
     */
    Person toEntity(PersonDTO dto, int depth, Person fromEntity);

    List<PersonDTO> loadAllAdvocates();
}
