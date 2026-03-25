package sud.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sud.domain.person.PersonDTO;

@Getter
@Setter
@ToString
public class LawsuitRegistrDTO {
    private PersonDTO zayav;
    private PersonDTO otv;
    private Long courtId;
    private String lawNum;
    private String lawName;
    private String lawDesc;
    private Boolean copyZayav;
    private Boolean copyOtv;
    private Long baseLawsuitId;
    private Integer clientType;
}
