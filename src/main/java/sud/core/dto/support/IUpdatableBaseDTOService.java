package sud.core.dto.support;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IUpdatableBaseDTOService<D extends BaseEntityDTO<P>, E, R extends JpaSpecificationExecutor<E> & JpaRepository<E, P>, P> extends IBaseEntityDTOService<D, E, R, P> {
    void deleteById(P var1);

    D save(D var1);
}
