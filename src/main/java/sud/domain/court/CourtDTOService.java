package sud.domain.court;

import sud.core.dto.support.IUpdatableBaseDTOService;
import sud.dto.CourtPersonLinkDTO;

public interface CourtDTOService extends IUpdatableBaseDTOService<CourtDTO, Court, CourtRepository, Long> {


 //   CourtDTO createNew();

    CourtDTO findOne(Long id);

    void updateFields(Court item, CourtDTO dto);

    CourtDTO save(CourtDTO dto);

    /**
     * Converts the passed Court to a DTO.
     */
    CourtDTO toDTO(Court item);

    /**
     * Converts the passed Court to a DTO.
     */
    default CourtDTO toDTO(Court item, int depth) {
        return toDTO(item, depth, new CourtDTO());
    }


    CourtDTO toDTO(Court item, int depth, CourtDTO fromDTO);

    /**
     * Converts the passed dto to a Court.
     * Convenient for query by example.
     */
    default Court toEntity(CourtDTO dto) {
        return toEntity(dto, 1);
    }

    /**
     * Converts the passed dto to a Court.
     * Convenient for query by example.
     */
    default Court toEntity(CourtDTO dto, int depth) {
        return toEntity(dto, depth, new Court());
    }

    /**
     * Converts the passed dto to a Court.
     * Convenient for query by example.
     */
    Court toEntity(CourtDTO dto, int depth, Court fromEntity);

    void saveLink(CourtPersonLinkDTO courtPersonLinkDTO);
}
