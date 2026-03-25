package sud.domain.incoming_doc;

import sud.core.dto.support.IUpdatableBaseDTOService;

public interface IncomingDocDTOService extends IUpdatableBaseDTOService<IncomingDocDTO, IncomingDoc, IncomingDocRepository, Long> {


 //   CourtDTO createNew();

    IncomingDocDTO findOne(Long id);

    void updateFields(IncomingDoc item, IncomingDocDTO dto);

    IncomingDocDTO save(IncomingDocDTO dto);

    /**
     * Converts the passed Court to a DTO.
     */
    IncomingDocDTO toDTO(IncomingDoc item);

    /**
     * Converts the passed Court to a DTO.
     */
    default IncomingDocDTO toDTO(IncomingDoc item, int depth) {
        return toDTO(item, depth, new IncomingDocDTO());
    }


    IncomingDocDTO toDTO(IncomingDoc item, int depth, IncomingDocDTO fromDTO);

    /**
     * Converts the passed dto to a Court.
     * Convenient for query by example.
     */
    default IncomingDoc toEntity(IncomingDocDTO dto) {
        return toEntity(dto, 1);
    }

    /**
     * Converts the passed dto to a Court.
     * Convenient for query by example.
     */
    default IncomingDoc toEntity(IncomingDocDTO dto, int depth) {
        return toEntity(dto, depth, new IncomingDoc());
    }

    /**
     * Converts the passed dto to a Court.
     * Convenient for query by example.
     */
    IncomingDoc toEntity(IncomingDocDTO dto, int depth, IncomingDoc fromEntity);

}
