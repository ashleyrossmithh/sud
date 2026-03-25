package sud.domain.attorney;

import sud.core.dto.support.IUpdatableBaseDTOService;
import sud.dto.AttorneyToPersonsDTO;

import java.util.List;

public interface AttorneyDTOService extends IUpdatableBaseDTOService<AttorneyDTO, Attorney, AttorneyRepository, Long> {


 //   CourtDTO createNew();

    AttorneyDTO findOne(Long id);

    void updateFields(Attorney item, AttorneyDTO dto);

    AttorneyDTO save(AttorneyDTO dto);

    /**
     * Converts the passed Court to a DTO.
     */
    AttorneyDTO toDTO(Attorney item);

    /**
     * Converts the passed Court to a DTO.
     */
    default AttorneyDTO toDTO(Attorney item, int depth) {
        return toDTO(item, depth, new AttorneyDTO());
    }


    AttorneyDTO toDTO(Attorney item, int depth, AttorneyDTO fromDTO);

    /**
     * Converts the passed dto to a Court.
     * Convenient for query by example.
     */
    default Attorney toEntity(AttorneyDTO dto) {
        return toEntity(dto, 1);
    }

    /**
     * Converts the passed dto to a Court.
     * Convenient for query by example.
     */
    default Attorney toEntity(AttorneyDTO dto, int depth) {
        return toEntity(dto, depth, new Attorney());
    }

    /**
     * Converts the passed dto to a Court.
     * Convenient for query by example.
     */
    Attorney toEntity(AttorneyDTO dto, int depth, Attorney fromEntity);

    void save(AttorneyToPersonsDTO attorneyToPersonsDTO);

    List<AttorneyDTO> getAllByLawsuitId(Long lawsuitId);
}
