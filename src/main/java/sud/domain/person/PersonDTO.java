package sud.domain.person;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sud.core.dto.support.BaseEntityDTO;
import sud.enums.DirectionType;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class PersonDTO extends BaseEntityDTO<Long> {
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

    @Deprecated
    private Long lawsuitId;

    private Integer type;

    private Integer direction;

    private String surname;

    private String firstName;

    private String patronymic;

    private String name;

    private LocalDate birthDate;

    private String phone;

    private String email;

    private String address;

    //TODO заполнять в зваисимостя от ответчика
    public boolean needCreateUser;

    public String getDirectionDesc() {
        if (direction == null) return "";
        return DirectionType.parse(direction).getName();
    }
}
