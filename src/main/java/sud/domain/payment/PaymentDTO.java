package sud.domain.payment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sud.core.dto.support.BaseEntityDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class PaymentDTO extends BaseEntityDTO<Long> {
    @JsonProperty("id")
    private Long id;

    private Long idOld;

    @JsonIgnore
    public boolean isIdSet() {
        return id != null;
    }

    @JsonIgnore
    public boolean isIdOldSet() {
        return idOld != null;
    }

    private Long lawsuitId; // судебное дело

    private LocalDate payDate;

    private String note;

    private BigDecimal paySumIn;
    private BigDecimal paySumOut;

    private String payPurpose;
}
