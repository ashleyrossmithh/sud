package sud.domain.user_data;

import jakarta.persistence.*;
import lombok.Data;
import sud.domain.person.Person;

@Data
@Entity
@Table(name = "user_data")
public class UserData {

    @Id
    @Column(length = 20, unique = true)
    private String login;

    @Column(nullable = false, length = 60)
    private String password;

    @OneToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "person_id", nullable = true)
    private Person person;

    // Если в других частях кода вызывается getId(),
    // возвращаем login, так как он является первичным ключом
    public String getId() {
        return this.login;
    }
}