package sud.domain.inoming_file;

import sud.core.dto.support.IUpdatableBaseDTOService;

import java.util.List;

public interface IncomingDocFileDTOService extends IUpdatableBaseDTOService<IncomingDocFileDTO, IncomingDocFile, IncomingDocFileRepository, Long> {

    IncomingDocFileDTO createNew();

    IncomingDocFileDTO findOne(Long id);

    void updateFields(IncomingDocFile incomingDoc, IncomingDocFileDTO dto);

    IncomingDocFileDTO save(IncomingDocFileDTO dto);

    /**
     * Converts the passed incomingDocFile to a DTO.
     */
    IncomingDocFileDTO toDTO(IncomingDocFile incomingDoc);

    /**
     * Converts the passed incomingDocFile to a DTO.
     */
    default IncomingDocFileDTO toDTO(IncomingDocFile incomingDoc, int depth) {
        return toDTO(incomingDoc, depth, new IncomingDocFileDTO());
    }

    /**
     * Converts the passed incomingDocFile to a DTO. The depth is used to control the
     * amount of association you want. It also prevents potential infinite serialization cycles.
     *
     * @param incomingDoc
     * @param depth the depth of the serialization. A depth equals to 0, means no x-to-one association will be serialized.
     *              A depth equals to 1 means that xToOne associations will be serialized. 2 means, xToOne associations of
     *              xToOne associations will be serialized, etc.
     */
    IncomingDocFileDTO toDTO(IncomingDocFile incomingDoc, int depth, IncomingDocFileDTO fromDTO);

    /**
     * Converts the passed dto to a IncomingDocFile.
     * Convenient for query by example.
     */
    default IncomingDocFile toEntity(IncomingDocFileDTO dto) {
        return toEntity(dto, 1);
    }

    /**
     * Converts the passed dto to a IncomingDocFile.
     * Convenient for query by example.
     */
    default IncomingDocFile toEntity(IncomingDocFileDTO dto, int depth) {
        return toEntity(dto, depth, new IncomingDocFile());
    }

    /**
     * Converts the passed dto to a IncomingDocFile.
     * Convenient for query by example.
     */
    IncomingDocFile toEntity(IncomingDocFileDTO dto, int depth, IncomingDocFile fromEntity);

    List<IncomingDocFile> findAllIncomingDocFilesByIncomingDoc(Long incomingDocId);

    IncomingDocFile findById(Long id);
}
