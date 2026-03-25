package sud.rest.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sud.domain.attorney.AttorneyDTO;
import sud.domain.attorney.AttorneyDTOService;
import sud.domain.person.PersonDTO;
import sud.domain.person.PersonDTOService;
import sud.domain.person.PersonDTOServiceImpl;
import sud.domain.person.PersonRepository;
import sud.dto.AttorneyToPersonsDTO;
import sud.dto.PersonWithParentDto;
import sud.services.PersonLinkService;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AttorneyResource {

    private final AttorneyDTOService attorneyDTOService;

    private final PersonLinkService personLinkService;
    private final PersonRepository personRepository;
    private final PersonDTOService personDTOService;

    @RequestMapping(
            value = {"/attorney/create"},
            method = {RequestMethod.POST},
            produces = {"application/json"}
    )
    public ResponseEntity<Void> create(@RequestBody AttorneyToPersonsDTO attorneyToPersonsDTO) throws URISyntaxException {
        attorneyDTOService.save(attorneyToPersonsDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @RequestMapping(value = "/attorney/{lawsuitId}", method = GET, produces = APPLICATION_JSON_VALUE)
    public List<AttorneyDTO> getAllByLawsuitId(@PathVariable Long lawsuitId) throws URISyntaxException {
        var items = attorneyDTOService.getAllByLawsuitId(lawsuitId);
        return items;
    }


    @RequestMapping(value = "/loadPersonByAttorneyId/{attorneyId}", method = GET, produces = APPLICATION_JSON_VALUE)
    public List<PersonDTO> findById(@PathVariable Long attorneyId) throws URISyntaxException {
        return personRepository.loadPersonByAttorneyId(attorneyId).stream().map(personDTOService:: toDTO).toList();
    }

}
