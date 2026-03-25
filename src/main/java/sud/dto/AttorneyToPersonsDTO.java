package sud.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sud.domain.attorney.AttorneyDTO;

import java.util.List;

@Getter
@Setter
@ToString
public class AttorneyToPersonsDTO {
    private List<Long> personIdList;
    private AttorneyDTO attorney;
}
