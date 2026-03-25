package sud.domain.lawsuit;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sud.domain.lawsuit.Lawsuit;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/entities") // Общий путь для всех ручек по заданию [cite: 65]
public class LawsuitController {

    @Autowired
    private LawsuitRepository lawsuitRepository;

    // 1. GET /entities - Получение списка всех дел [cite: 65, 66]
    @GetMapping
    public List<Lawsuit> getAllEntities() {
        return lawsuitRepository.findAll(); // Возвращает массив объектов [cite: 68]
    }

    // 2. GET /entities/{id} - Получение одного дела по ID [cite: 70, 71]
    @GetMapping("/{id}")
    public ResponseEntity<Lawsuit> getEntityById(@PathVariable Long id) {
        return lawsuitRepository.findById(id)
                .map(ResponseEntity::ok) // 200 OK [cite: 74]
                .orElse(ResponseEntity.notFound().build()); // 404 Not Found [cite: 76]
    }

    // 3. POST /entities - Создание нового дела [cite: 77, 78]
    @PostMapping
    public ResponseEntity<Lawsuit> createEntity(@Valid @RequestBody Lawsuit lawsuit) {
        Lawsuit saved = lawsuitRepository.save(lawsuit);
        return new ResponseEntity<>(saved, HttpStatus.CREATED); // 201 Created [cite: 81]
    }

    // 4. PUT /entities/{id} - Полное обновление [cite: 84, 85]
    @PutMapping("/{id}")
    public ResponseEntity<Lawsuit> updateEntity(@PathVariable Long id, @Valid @RequestBody Lawsuit lawsuitDetails) {
        return lawsuitRepository.findById(id).map(lawsuit -> {
            lawsuit.setLsName(lawsuitDetails.getLsName());
            lawsuit.setLsNumber(lawsuitDetails.getLsNumber());
            lawsuit.setLsDescription(lawsuitDetails.getLsDescription());
            lawsuit.setClaimAmount(lawsuitDetails.getClaimAmount());
            lawsuit.setActive(lawsuitDetails.getActive());
            return ResponseEntity.ok(lawsuitRepository.save(lawsuit)); // 200 OK [cite: 90]
        }).orElse(ResponseEntity.notFound().build()); // 404 Not Found [cite: 91]
    }

    // 5. PATCH /entities/{id} - Частичное обновление [cite: 94, 95]
    @PatchMapping("/{id}")
    public ResponseEntity<Lawsuit> patch(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return lawsuitRepository.findById(id).map(lawsuit -> {
            if (updates.get("lsName") != null) lawsuit.setLsName(updates.get("lsName").toString());

            // Вот тут чаще всего падает! Добавляем проверку:
            if (updates.containsKey("claimAmount") && updates.get("claimAmount") != null) {
                try {
                    lawsuit.setClaimAmount(Double.valueOf(updates.get("claimAmount").toString()));
                } catch (Exception e) {
                    // Если пришла фигня вместо числа - просто игнорим
                }
            }

            if (updates.get("active") != null) {
                lawsuit.setActive(Boolean.valueOf(updates.get("active").toString()));
            }

            return ResponseEntity.ok(lawsuitRepository.save(lawsuit));
        }).orElse(ResponseEntity.notFound().build());
    }

    // 6. DELETE /entities/{id} - Удаление [cite: 101, 102]
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntity(@PathVariable Long id) {
        if (lawsuitRepository.existsById(id)) {
            lawsuitRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // 204 No Content [cite: 106]
        }
        return ResponseEntity.notFound().build(); // 404 Not Found [cite: 107]
    }
}