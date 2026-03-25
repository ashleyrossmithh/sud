package sud.core.rest.support;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sud.core.dto.support.IBaseDTOService;

import java.util.List;

public interface CanCompleteResource <D, S extends IBaseDTOService<D>> {
    S getDTOService();

    @GetMapping(
            value = {"/complete/all"},
            produces = {"application/json"}
    )
    default List<D> complete() {
        List<D> results = this.getDTOService().complete("", -1, null);
        return results;
    }

}
