package sud.domain.inoming_file;

import sud.core.bc.filter.api.FilterExpression;
import sud.core.dto.support.PageRequestByFilter;
import sud.core.dto.support.PageResponse;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class IncomingDocFileDTOServiceImpl implements IncomingDocFileDTOService {
    @Autowired
    private IncomingDocFileRepository incomingDocFileRepository;

//    private IncomingDocDTOServiceImpl incomingDocDTOServiceImpl;
//    private IncomingDocRepository incomingDocRepository;

    @Transactional()
    public IncomingDocFileDTO createNew() {
        IncomingDocFile val = new IncomingDocFile();//.withDefaults();
    //    val.setId(incomingDocFileRepository.getNewId());
        final IncomingDocFileDTO dto;
        dto = toDTO(val);
        dto.setIdOld(null);
        return dto;
    }

    @Transactional(readOnly = true)
    public IncomingDocFileDTO findOne(Long id) {
        IncomingDocFileDTO dto = toDTO(incomingDocFileRepository.findById(id).orElse(null));
        return dto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<IncomingDocFileDTO> complete(String query, int maxResults, FilterExpression templateExpressions) {
        List<IncomingDocFile> results = new ArrayList<>();
   /*     FilterExpression filter = new FilterExpression(new ArrayList<>(), ChainOperationEnum.CO_AND);
        FilterExpression attrFilter = new FilterExpression(new ArrayList<>(), ChainOperationEnum.CO_OR);
        attrFilter.getChildren().add(new FilterExpression(new FilterCriteria("idfFileName", FilterOperationEnum.FO_LIKE, query, ChainOperationEnum.CO_OR, 1),
                ChainOperationEnum.CO_OR));

        if (templateExpressions != null)
            filter.getChildren().add(templateExpressions);
        if (!attrFilter.getChildren().isEmpty())
            filter.getChildren().add(attrFilter);
        results = filterService.findPage(filter, PageRequest.of(0, maxResults, Sort.by("idfFileName")), incomingDocFileRepository).getContent();
*/
        return results.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public PageResponse<IncomingDocFileDTO> flexFilter(PageRequestByFilter<FilterExpression> filter) {


        return null;
    }/*  PageRequest pageRequest = (PageRequest) filter.toPageable();
        Page<IncomingDocFile> page = filterService.findPage(filter.getExample(), pageRequest, incomingDocFileRepository);
        List<IncomingDocFileDTO> content = page.getContent().stream().map(this::toDTO).toList();
        return new PageResponse<>(page.getTotalPages(), page.getTotalElements(), content);*/

    public void updateFields(IncomingDocFile incomingDoc, IncomingDocFileDTO dto) {

        incomingDoc.setId(dto.getId());
        incomingDoc.setIdfFileName(dto.getIdfFileName());
        incomingDoc.setIdfContent(dto.getIdfContent());
        incomingDoc.setIdfNote(dto.getIdfNote());
        incomingDoc.setIncomingDocId(dto.getIncomingDocId());
        //IncomingDoc
     /*   if (dto.getIncomingDoc() == null) {
            incomingDocFile.setIncomingDoc(null);
        } else {
            IncomingDoc incomingDoc = incomingDocFile.getIncomingDoc();
            if (incomingDoc == null || (incomingDoc.getId().compareTo(dto.getIncomingDoc().getId()) != 0)) {
                incomingDocFile.setIncomingDoc(incomingDocRepository.findById(dto.getIncomingDoc().getId()).orElse(null));
            }
        }*/

    }

    @Override
    public void deleteById(Long var1) {

    }

    /**
     * Save the passed dto as a new entity or update the corresponding entity if any.
     */
    @Transactional
    public IncomingDocFileDTO save(IncomingDocFileDTO dto) {
        if (dto == null) {
            return null;
        }
        final IncomingDocFile incomingDoc;

        if (dto.isIdOldSet()) {
            IncomingDocFile incomingDocTmp = incomingDocFileRepository.findById(dto.getIdOld()).orElse(null);
            if (incomingDocTmp != null) {
                incomingDoc = incomingDocTmp;
            } else {
                incomingDoc = new IncomingDocFile();
                incomingDoc.setId(dto.getIdOld());
            }
            IncomingDocFileDTO oldValue = toDTO(incomingDoc);
            //auditLogService.logEntityEvent(editEventSortId, incomingDocFile.getId().toString(), objTypeCode, AuditEntityEvent.UPDATE, dto,
            //        toDTO(incomingDocFile), "");
        } else {
            if (dto.getId() != null && incomingDocFileRepository.findById(dto.getId()).orElse(null) != null)
                throw new ValidationException("Запись с данным ключом  " + dto.getId() + " уже существует");
            else
                incomingDoc = new IncomingDocFile();
           // auditLogService.logEntityEvent(createEventSortId, Objects.requireNonNull(dto.getId()).toString(), objTypeCode, AuditEntityEvent.CREATE, dto, null,
           //         "");
        }

        updateFields(incomingDoc, dto);

        IncomingDocFileDTO result = toDTO(incomingDocFileRepository.save(incomingDoc));

        return result;
    }

    /**
     * Converts the passed incomingDocFile to a DTO.
     */
    public IncomingDocFileDTO toDTO(IncomingDocFile incomingDoc) {
        return toDTO(incomingDoc, 1);
    }

    /**
     * Converts the passed incomingDocFile to a DTO.
     */
    public IncomingDocFileDTO toDTO(IncomingDocFile incomingDoc, int depth) {
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
    public IncomingDocFileDTO toDTO(IncomingDocFile incomingDoc, int depth, IncomingDocFileDTO fromDTO) {
        if (incomingDoc == null) {
            return null;
        }

        IncomingDocFileDTO dto = fromDTO;

        dto.setId(incomingDoc.getId());

        dto.setIdOld(incomingDoc.getId());
        dto.setIdfFileName(incomingDoc.getIdfFileName());

        dto.setIdfContent(incomingDoc.getIdfContent());

        dto.setIdfNote(incomingDoc.getIdfNote());
        dto.setIncomingDocId(incomingDoc.getIncomingDocId());

        if (depth-- > 0) {
//            dto.setIncomingDoc(incomingDocDTOServiceImpl.toDTO(incomingDocFile.getIncomingDoc(), depth));

        }
        return dto;
    }

    /**
     * Converts the passed dto to a IncomingDocFile.
     * Convenient for query by example.
     */

    public IncomingDocFile toEntity(IncomingDocFileDTO dto) {
        return toEntity(dto, 1);
    }

    @Override
    public List<IncomingDocFileDTO> search(String var1, Object var2, int var3) {
        return List.of();
    }

    /**
     * Converts the passed dto to a IncomingDocFile.
     * Convenient for query by example.
     */
    public IncomingDocFile toEntity(IncomingDocFileDTO dto, int depth) {
        return toEntity(dto, depth, new IncomingDocFile());
    }

    protected IncomingDocFileRepository getEntityRepository() {
        return incomingDocFileRepository;
    }

    @Override
    public IncomingDocFile toEntity(IncomingDocFileDTO dto, int depth, IncomingDocFile fromEntity) {
        if (dto == null) {
            return null;
        }

        updateFields(fromEntity, dto);
        if (depth-- > 0) {
//            fromEntity.setIncomingDoc(incomingDocDTOServiceImpl.toEntity(dto.getIncomingDoc(), depth));
        }

        return fromEntity;
    }

    @Override
    public List<IncomingDocFile> findAllIncomingDocFilesByIncomingDoc(Long incomingDocId) {
        return incomingDocFileRepository.findAllByIncomingDocId(incomingDocId);
    }

    @Override
    public IncomingDocFile findById(Long id) {
        return incomingDocFileRepository.findById(id).orElseThrow();
    }
}

