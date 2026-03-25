package sud.rest.domain;

import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;
import sud.core.rest.support.CanUpdateResource;
import sud.domain.inoming_file.IncomingDocFile;
import sud.domain.inoming_file.IncomingDocFileDTO;
import sud.domain.inoming_file.IncomingDocFileDTOService;
import sud.domain.inoming_file.IncomingDocFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api/entities/incomingDocFiles")
@Log4j2
@RequiredArgsConstructor
public class IncomingDocFileResource // extends BaseResource<IncomingDocFileDTO, IncomingDocFileDTOService>
        implements CanUpdateResource<IncomingDocFileDTO, IncomingDocFileDTOService, IncomingDocFile, IncomingDocFileRepository, Long> {
    //CanFilterAndCompleteResource<IncomingDocFileDTO, IncomingDocFileDTOService>,

    @Autowired
    IncomingDocFileDTOService dtoService;

    public String getApiUrl() {
        return "/api/entities/incomingDocFiles/";
    }

    @Override
    public IncomingDocFileDTOService getDTOService() {
        return dtoService;
    }


    @RequestMapping(value = "/{id:.+}", method = DELETE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable Long id) throws URISyntaxException {
        log.debug("Delete by id IncomingDocFile : {}", id);
        getDTOService().deleteById(id);
        return ResponseEntity.ok().build();
    }


    @RequestMapping(value = "/{id:.+}", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<IncomingDocFileDTO> findById(@PathVariable Long id) throws URISyntaxException {
        log.debug("Find by id IncomingDocFile : {}", id);
        return Optional.ofNullable(getDTOService().findOne(id)).map(incomingDocFileDTO -> new ResponseEntity<>(incomingDocFileDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/all/{incomingDocId}", produces = APPLICATION_JSON_VALUE)
    public List<IncomingDocFile> findAllIncomigDocFiles(@PathVariable Long incomingDocId) {
        return getDTOService().findAllIncomingDocFilesByIncomingDoc(incomingDocId);
    }

    @Transactional(readOnly = true)
    @GetMapping(value = "/{id}/download/idFileContent")
    @ResponseBody
    public ResponseEntity<byte[]> cdDataFileDownload(@PathVariable Long id) {
        IncomingDocFile idf = getDTOService().findById(id);
        log.debug("File Download: {}", idf.getIdfFileName());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + idf.getIdfFileName() + "\"").body(idf.getIdfContent());
    }
}
