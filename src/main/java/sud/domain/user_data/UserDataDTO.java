package sud.domain.user_data;

import lombok.*;
import sud.domain.person.PersonDTO;
import sud.enums.DirectionType;

@Getter
@Setter
@ToString
@Builder
public class UserDataDTO {
    private String login;
    private String password;
    private String passwordNew;
    private PersonDTO person;

    public String getRoleName() { //используется в UI
        if (person == null) return "Администратор";
        if (DirectionType.parse(person.getDirection()).isAdvocateGroup()) return DirectionType.HELPER.getName();
        return "Клиент";
    }
}
