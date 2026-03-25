package sud.rest.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sud.core.rest.support.CanCompleteResource;
import sud.core.rest.support.CanUpdateResource;
import sud.domain.lawsuit.Lawsuit;
import sud.domain.lawsuit.LawsuitDTO;
import sud.domain.lawsuit.LawsuitDTOService;
import sud.domain.lawsuit.LawsuitRepository;
import sud.dto.LawsuitRegistrDTO;

import java.net.URISyntaxException;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api/entities/lawsuit")
@Log4j2
@RequiredArgsConstructor
public class LawsuitResource implements CanUpdateResource<LawsuitDTO, LawsuitDTOService, Lawsuit, LawsuitRepository, Long>,
        CanCompleteResource<LawsuitDTO, LawsuitDTOService> {
    private final LawsuitDTOService lawsuitDTOService;

    @Override
    public String getApiUrl() {
        return "/api/entities/lawsuit/";
    }

    @Override
    public LawsuitDTOService getDTOService() {
        return lawsuitDTOService;
    }

    @RequestMapping(value = "/{id:.+}", method = DELETE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable Long id) throws URISyntaxException {
        log.debug("Delete by id Court : {}", id);
        getDTOService().deleteById(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id:.+}", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<LawsuitDTO> findById(@PathVariable Long id) throws URISyntaxException {
        log.debug("Find by id CourtDTO : {}", id);
        return Optional.ofNullable(getDTOService().findOne(id)).map(dtoItem -> new ResponseEntity<>(dtoItem, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(
            value = {"/createRegistryData"},
            method = {RequestMethod.POST},
            produces = {"application/json"}
    )
    public ResponseEntity<Void> createNew(@RequestBody LawsuitRegistrDTO lawsuitRegistrDTO) throws URISyntaxException {
        log.info(lawsuitRegistrDTO);
        this.lawsuitDTOService.save(lawsuitRegistrDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
