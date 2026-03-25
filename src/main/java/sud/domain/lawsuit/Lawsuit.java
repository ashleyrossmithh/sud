package sud.domain.lawsuit;

import jakarta.persistence.*;
import jakarta.validation.constraints.*; // Важно для валидации
import lombok.Data;
import sud.domain.CourtPersonLawsuitLink;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "lawsuit")
public class Lawsuit implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1. Строковое обязательное поле с валидацией (длина от 2 до 100)
    @NotBlank(message = "Название дела не может быть пустым")
    @Size(min = 2, max = 100, message = "Название должно быть от 2 до 100 символов")
    @Column(length = 200, nullable = false)
    private String lsName;

    @Column(length = 20)
    private String lsNumber;

    @Column(length = 1000)
    private String lsDescription;

    @Column(nullable = false)
    private Long courtId;

    @Column(length = 20)
    private String regNumber;

    // 2. Числовое поле (обязательно по заданию)
    @Min(value = 0, message = "Сумма иска не может быть отрицательной")
    private Double claimAmount; // Сумма иска

    // 3. Поле-перечисление или статус
    private Integer lsResult;

    // 4. Булево поле (обязательно по заданию)
    private Boolean active = true;

    // 5. Поле даты/времени (обязательно по заданию)
    @NotNull
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "lawsuit", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, optional = true)
    private CourtPersonLawsuitLink courtPersonLawsuitLink;

    // Автоматическая установка даты при создании
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}