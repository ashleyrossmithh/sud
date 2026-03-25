package sud.rest.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sud.core.rest.support.CanUpdateResource;
import sud.domain.incoming_doc.IncomingDoc;
import sud.domain.incoming_doc.IncomingDocDTO;
import sud.domain.incoming_doc.IncomingDocDTOService;
import sud.domain.incoming_doc.IncomingDocRepository;


import java.net.URISyntaxException;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api/entities/incomingDoc")
@Log4j2
@RequiredArgsConstructor
public class IncomingDocResource // extends BaseResource<IncomingDocFileDTO, IncomingDocFileDTOService>
        implements CanUpdateResource<IncomingDocDTO, IncomingDocDTOService, IncomingDoc, IncomingDocRepository, Long> {
    //CanFilterAndCompleteResource<IncomingDocFileDTO, IncomingDocFileDTOService>,

    @Autowired
    IncomingDocDTOService dtoService;

    public String getApiUrl() {
        return "/api/entities/incomingDoc/";
    }

    @Override
    public IncomingDocDTOService getDTOService() {
        return dtoService;
    }


    @RequestMapping(value = "/{id:.+}", method = DELETE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable Long id) throws URISyntaxException {
        log.debug("Delete by id IncomingDocFile : {}", id);
        getDTOService().deleteById(id);
        return ResponseEntity.ok().build();
    }


    @RequestMapping(value = "/{id:.+}", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<IncomingDocDTO> findById(@PathVariable Long id) throws URISyntaxException {
        log.debug("Find by id IncomingDocFile : {}", id);
        return Optional.ofNullable(getDTOService().findOne(id)).map(incomingDocDTO -> new ResponseEntity<>(incomingDocDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
