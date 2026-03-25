package sud.core.rest.support;

import sud.core.dto.support.BaseEntityDTO;
import sud.core.dto.support.IUpdatableBaseDTOService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.URI;
import java.net.URISyntaxException;

public interface CanCreateResource<D extends BaseEntityDTO<P>, S extends IUpdatableBaseDTOService<D, E, R, P>, E, R extends JpaRepository<E, P> & JpaSpecificationExecutor<E>, P> {
    String getApiUrl();

    S getDTOService();

    @RequestMapping(
            value = {"/createNew"},
            method = {RequestMethod.POST},
            produces = {"application/json"}
    )
    default ResponseEntity<D> create() throws URISyntaxException {
        D result = this.getDTOService().createNew();
        return ResponseEntity.created(new URI(this.getApiUrl() + "new")).body(result);
    }


    @RequestMapping(
            value = {"/create"},
            method = {RequestMethod.POST},
            produces = {"application/json"}
    )
    default ResponseEntity<D> createNew(@RequestBody D dto) throws URISyntaxException {
        if (dto.isIdSet()) {
            return ResponseEntity.badRequest()
                    .header("Failure", new String[]{"Cannot create" + dto.getClass().getSimpleName() + "  with existing ID"})
                    .body(null);
        } else {
            D result = this.getDTOService().save(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        }
    }


   @RequestMapping(
            value = {"/"},
            method = {RequestMethod.POST},
            produces = {"application/json"}
    )
    default ResponseEntity<D> create(@RequestBody D dto) throws URISyntaxException {
        if (dto.isIdSet()) {
            return ResponseEntity.badRequest()
                    .header("Failure", new String[]{"Cannot create" + dto.getClass().getSimpleName() + "  with existing ID"})
                    .body(null);
        } else {
            D result = this.getDTOService().save(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        }
    }
}
