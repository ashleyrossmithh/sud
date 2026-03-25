package sud.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class PersonWithLinkDTO {
    private List<Long> personIds;
    private Long courtSessionId;
    private Boolean needPresence;
}
