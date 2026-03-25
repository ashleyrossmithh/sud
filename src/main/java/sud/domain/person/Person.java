package sud.domain.person;

import jakarta.persistence.*;
import lombok.Data;
import sud.domain.user_data.UserData;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "person")//Данные ФЛ, ЮЛ
public class Person {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Integer type; //тип ФЛ, ЮЛ

    private Integer direction; //1 заявитель, 2 ответчик, 3 представитель, 4 судья,.. (DirectionType)

    @Column(length = 20)
    private String surname;

    @Column(length = 20)
    private String firstName;

    @Column(length = 20)
    private String patronymic;

    @Column(length = 60)
    private String name;

    private LocalDate birthDate;

    @Column(length = 20)
    private String phone;

    @Column(length = 20)
    private String email;

    private String address;

    @OneToOne(mappedBy = "person", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private UserData userData;

}
