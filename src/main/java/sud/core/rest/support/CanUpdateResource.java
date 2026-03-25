package sud.core.rest.support;

import sud.core.dto.support.BaseEntityDTO;
import sud.core.dto.support.IUpdatableBaseDTOService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.URISyntaxException;

public interface CanUpdateResource<D extends BaseEntityDTO<P>,
        S extends IUpdatableBaseDTOService<D, E1, R, P>,
        E1,
        R extends JpaRepository<E1, P> & JpaSpecificationExecutor<E1>, P>
        extends CanCreateResource<D, S, E1, R, P> {

    @RequestMapping(
            value = {"/"},
            method = {RequestMethod.PUT},
            produces = {"application/json"}
    )
    default ResponseEntity<D> update(@RequestBody D entity) throws URISyntaxException {
        if (!entity.isIdSet()) {
            return this.create(entity);
        } else {
            D result = this.getDTOService().save(entity);
            return ResponseEntity.ok().body(result);
        }
    }

    @RequestMapping(
            value = {"/update"},
            method = {RequestMethod.PUT},
            produces = {"application/json"}
    )
    default ResponseEntity<D> updateNew(@RequestBody D entity) throws URISyntaxException {
        D result = this.getDTOService().save(entity);
        return ResponseEntity.ok().body(result);
    }

}
