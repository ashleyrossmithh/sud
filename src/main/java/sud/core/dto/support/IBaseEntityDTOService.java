package sud.core.dto.support;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;
import java.util.List;

public interface IBaseEntityDTOService<D, E, R extends JpaSpecificationExecutor<E> & JpaRepository<E, P>, P> extends IBaseDTOService<D> {
    D toDTO(E var1);

    E toEntity(D var1);

 //   BigDecimal getSum(SumFieldRequest var1);

    List<D> search(String var1, Object var2, int var3);
}
