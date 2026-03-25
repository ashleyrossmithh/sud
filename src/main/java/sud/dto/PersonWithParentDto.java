package sud.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sud.domain.person.PersonDTO;

@Getter
@Setter
@ToString
public class PersonWithParentDto {
    private PersonDTO personDTO;
    private Long parentId;

}
