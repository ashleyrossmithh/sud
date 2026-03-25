package sud.rest.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import sud.core.rest.support.CanCompleteResource;
import sud.core.rest.support.CanUpdateResource;
import sud.domain.court.Court;
import sud.domain.court.CourtDTO;
import sud.domain.court.CourtDTOService;
import sud.domain.court.CourtRepository;
import sud.dto.CourtPersonLinkDTO;
import sud.excel.ExportColumnSetting;
import sud.excel.ExportService;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api/entities/court")
@Log4j2
@RequiredArgsConstructor
public class CourtResource implements CanUpdateResource<CourtDTO, CourtDTOService, Court, CourtRepository, Long>,
        CanCompleteResource<CourtDTO, CourtDTOService> {

    private final CourtDTOService courtDTOService;
    private final CourtRepository courtRepository;
    private final ExportService exportService;

    @Override
    public String getApiUrl() {
        return "/api/entities/court/";
    }

    @Override
    public CourtDTOService getDTOService() {
        return courtDTOService;
    }

    @RequestMapping(value = {"", "/"}, method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CourtDTO>> getAll() {
        log.debug("REST request to get all Courts");
        return new ResponseEntity<>(complete(), HttpStatus.OK);
    }

    @PostMapping(value = {"", "/"}, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CourtDTO> create(@RequestBody CourtDTO courtDTO) throws URISyntaxException {
        log.info("REST request to save Court via POST : {}", courtDTO);

        validateCourt(courtDTO);

        CourtDTO result = getDTOService().save(courtDTO);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id:.+}", produces = APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<CourtDTO> update(@PathVariable Long id, @RequestBody CourtDTO courtDTO) {
        log.info("REST request to update Court via PUT : {}, id: {}", courtDTO, id);

        validateCourt(courtDTO);

        Court entity = courtRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Court not found"));

        entity.setShortName(courtDTO.getShortName());
        entity.setFullName(courtDTO.getFullName());
        entity.setAddress(courtDTO.getAddress());
        entity.setTelNumber(courtDTO.getTelNumber());
        entity.setSite(courtDTO.getSite());
        entity.setCourtType(courtDTO.getCourtType());

        Court saved = courtRepository.save(entity);

        CourtDTO result = courtDTOService.findOne(saved.getId());

        return ResponseEntity.ok(result);
    }

    @PatchMapping(value = "/{id:.+}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CourtDTO> partialUpdate(@PathVariable Long id, @RequestBody CourtDTO courtDTO) {
        return update(id, courtDTO);
    }

    @RequestMapping(value = "/{id:.+}", method = DELETE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable Long id) throws URISyntaxException {
        log.debug("Delete by id Court : {}", id);
        getDTOService().deleteById(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id:.+}", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CourtDTO> findById(@PathVariable Long id) throws URISyntaxException {
        log.debug("Find by id CourtDTO : {}", id);
        return Optional.ofNullable(getDTOService().findOne(id))
                .map(dtoItem -> new ResponseEntity<>(dtoItem, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/createCourtPersonLink", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createNew(@RequestBody CourtPersonLinkDTO courtPersonLinkDTO) throws URISyntaxException {
        log.info("Creating link: {}", courtPersonLinkDTO);
        getDTOService().saveLink(courtPersonLinkDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Transactional(readOnly = true)
    @PostMapping("/exportExcel")
    public ResponseEntity<byte[]> export(@RequestBody(required = false) List<ExportColumnSetting> settings) {
        log.info(">>> ГЕНЕРАЦИЯ ФАЙЛА ВРУЧНУЮ...");
        try (org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook()) {

            var sheet = workbook.createSheet("Суды");
            var header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Название");
            header.createCell(2).setCellValue("Адрес");

            List<CourtDTO> items = complete();
            int rowIdx = 1;
            for (CourtDTO item : items) {
                var row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(item.getId() != null ? item.getId().toString() : "");
                row.createCell(1).setCellValue(item.getShortName() != null ? item.getShortName() : "");
                row.createCell(2).setCellValue(item.getAddress() != null ? item.getAddress() : "");
            }

            java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
            workbook.write(out);
            byte[] content = out.toByteArray();

            log.info("<<< ФАЙЛ СФОРМИРОВАН: {} байт", content.length);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"courts.xlsx\"")
                    .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                    .body(content);

        } catch (Exception e) {
            log.error("!! ОШИБКА:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private void validateCourt(CourtDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Тело запроса пустое");
        }

        if (dto.getShortName() == null || dto.getShortName().isBlank()) {
            throw new IllegalArgumentException("shortName обязателен");
        }

        if (dto.getFullName() == null || dto.getFullName().isBlank()) {
            throw new IllegalArgumentException("fullName обязателен");
        }

        if (dto.getAddress() == null || dto.getAddress().isBlank()) {
            throw new IllegalArgumentException("address обязателен");
        }

        if (dto.getCourtType() == null) {
            throw new IllegalArgumentException("courtType обязателен");
        }

        if (dto.getTelNumber() != null && dto.getTelNumber().length() > 20) {
            throw new IllegalArgumentException("Слишком длинный номер телефона");
        }
    }
}