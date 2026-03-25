package sud.rest.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sud.domain.attorney.AttorneyDTO;
import sud.domain.payment.PaymentDTO;
import sud.domain.payment.PaymentDTOService;
import sud.domain.person.PersonDTO;
import sud.dto.AttorneyToPersonsDTO;
import sud.services.PersonLinkService;

import java.net.URISyntaxException;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentResource {


    private final PaymentDTOService paymentDTOService;

    @RequestMapping(
            value = {"/payment/create"},
            method = {RequestMethod.POST},
            produces = {"application/json"}
    )
    public ResponseEntity<Void> create(@RequestBody PaymentDTO paymentDTO) throws URISyntaxException {
        paymentDTOService.save(paymentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @RequestMapping(value = "/payment/{lawsuitId}", method = GET, produces = APPLICATION_JSON_VALUE)
    public List<PaymentDTO> getAllByLawsuitId(@PathVariable Long lawsuitId) throws URISyntaxException {
        return paymentDTOService.getAllByLawsuitId(lawsuitId);
    }


}
