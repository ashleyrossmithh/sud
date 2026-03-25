package sud.domain.court;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "court")//суд
public class Court implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Integer courtType;

    @Column(nullable = false, length = 60)
    private String shortName;

    @Column(length = 255)
    private String fullName;

    @Column(length = 200)
    private String address;

    @Column(length = 20)
    private String site;

    @Column(length = 12)
    private String telNumber;

}
