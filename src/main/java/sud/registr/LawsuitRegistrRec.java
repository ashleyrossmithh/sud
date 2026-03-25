package sud.registr;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class LawsuitRegistrRec {
    private Long id;
    private String lsDescription;
    private String lsName;
    private String lsNumber;
    private String lsRegNum;
    private String shortName;
    private String fullName;
    private String site;
    private String telNumber;
    private String address;
    private String courtSessionDate;
    private String zaName;
    private String zaFio;
    private String zaPhone;
    private String zaEmail;
    private String zaAddress;
    private String otvName;
    private String otvFio;
    private String otvPhone;
    private String otvEmail;
    private String otvAddress;
    private Integer statusCode;
    private String statusDesc;
    private LocalDateTime statusDateTime;
    private BigDecimal paySumInSum;
    private BigDecimal paySumOutSum;
    private BigDecimal resultSum;
    private Long courtId;
    private String courtPersonFio;
    private Long zaId;
    private Long otvId;
    private Boolean zaClientFlag;
    private Boolean otvClientFlag;
}
