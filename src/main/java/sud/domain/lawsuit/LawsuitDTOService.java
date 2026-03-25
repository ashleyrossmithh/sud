package sud.domain.lawsuit;

import sud.core.dto.support.IUpdatableBaseDTOService;
import sud.dto.LawsuitRegistrDTO;

public interface LawsuitDTOService extends IUpdatableBaseDTOService<LawsuitDTO, Lawsuit, LawsuitRepository, Long> {


    LawsuitDTO findOne(Long id);

    void updateFields(Lawsuit item, LawsuitDTO dto);

    LawsuitDTO save(LawsuitDTO dto);

    /**
     * Converts the passed Court to a DTO.
     */
    LawsuitDTO toDTO(Lawsuit item);

    /**
     * Converts the passed Court to a DTO.
     */
    default LawsuitDTO toDTO(Lawsuit item, int depth) {
        return toDTO(item, depth, new LawsuitDTO());
    }


    LawsuitDTO toDTO(Lawsuit item, int depth, LawsuitDTO fromDTO);

    /**
     * Converts the passed dto to a Court.
     * Convenient for query by example.
     */
    default Lawsuit toEntity(LawsuitDTO dto) {
        return toEntity(dto, 1);
    }

    /**
     * Converts the passed dto to a Court.
     * Convenient for query by example.
     */
    default Lawsuit toEntity(LawsuitDTO dto, int depth) {
        return toEntity(dto, depth, new Lawsuit());
    }

    /**
     * Converts the passed dto to a Court.
     * Convenient for query by example.
     */
    Lawsuit toEntity(LawsuitDTO dto, int depth, Lawsuit fromEntity);

    void save(LawsuitRegistrDTO dto);

}
