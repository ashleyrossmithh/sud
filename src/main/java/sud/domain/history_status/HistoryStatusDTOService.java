package sud.domain.history_status;

import sud.core.dto.support.IUpdatableBaseDTOService;

import java.util.List;

public interface HistoryStatusDTOService extends IUpdatableBaseDTOService<HistoryStatusDTO, HistoryStatus, HistoryStatusRepository, Long> {


    //   CourtDTO createNew();

    HistoryStatusDTO findOne(Long id);

    void updateFields(HistoryStatus item, HistoryStatusDTO dto);

    HistoryStatusDTO save(HistoryStatusDTO dto);

    /**
     * Converts the passed Court to a DTO.
     */
    HistoryStatusDTO toDTO(HistoryStatus item);

    /**
     * Converts the passed Court to a DTO.
     */
    default HistoryStatusDTO toDTO(HistoryStatus item, int depth) {
        return toDTO(item, depth, new HistoryStatusDTO());
    }


    HistoryStatusDTO toDTO(HistoryStatus item, int depth, HistoryStatusDTO fromDTO);

    /**
     * Converts the passed dto to a Court.
     * Convenient for query by example.
     */
    default HistoryStatus toEntity(HistoryStatusDTO dto) {
        return toEntity(dto, 1);
    }

    /**
     * Converts the passed dto to a Court.
     * Convenient for query by example.
     */
    default HistoryStatus toEntity(HistoryStatusDTO dto, int depth) {
        return toEntity(dto, depth, new HistoryStatus());
    }

    /**
     * Converts the passed dto to a Court.
     * Convenient for query by example.
     */
    HistoryStatus toEntity(HistoryStatusDTO dto, int depth, HistoryStatus fromEntity);

    List<HistoryStatusDTO> getAllByLawsuitId(Long lawsuitId);
}
