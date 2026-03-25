package sud.domain.payment;

import jakarta.persistence.*;
import lombok.Data;
import sud.domain.lawsuit.Lawsuit;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "payment") //платежи
public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @JoinColumn(name = "lawsuit_id", nullable = false)
    @ManyToOne
    private Lawsuit lawsuit; // судебное дело

    private LocalDate payDate;

    private String note;

    private BigDecimal paySumIn;

    private BigDecimal paySumOut;

    private String payPurpose;

}
