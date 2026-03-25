package sud.domain.court_session;

import sud.core.dto.support.IUpdatableBaseDTOService;
import sud.domain.attorney.AttorneyDTO;

import java.util.List;

public interface CourtSessionDTOService extends IUpdatableBaseDTOService<CourtSessionDTO, CourtSession, CourtSessionRepository, Long> {


 //   CourtDTO createNew();

    CourtSessionDTO findOne(Long id);

    void updateFields(CourtSession item, CourtSessionDTO dto);

    CourtSessionDTO save(CourtSessionDTO dto);

    /**
     * Converts the passed Court to a DTO.
     */
    CourtSessionDTO toDTO(CourtSession item);

    /**
     * Converts the passed Court to a DTO.
     */
    default CourtSessionDTO toDTO(CourtSession item, int depth) {
        return toDTO(item, depth, new CourtSessionDTO());
    }


    CourtSessionDTO toDTO(CourtSession item, int depth, CourtSessionDTO fromDTO);

    /**
     * Converts the passed dto to a Court.
     * Convenient for query by example.
     */
    default CourtSession toEntity(CourtSessionDTO dto) {
        return toEntity(dto, 1);
    }

    /**
     * Converts the passed dto to a Court.
     * Convenient for query by example.
     */
    default CourtSession toEntity(CourtSessionDTO dto, int depth) {
        return null;
    }

    /**
     * Converts the passed dto to a Court.
     * Convenient for query by example.
     */
    CourtSession toEntity(CourtSessionDTO dto, int depth, CourtSession fromEntity);

    List<CourtSessionDTO> getAllByLawsuitId(Long lawsuitId);
}
