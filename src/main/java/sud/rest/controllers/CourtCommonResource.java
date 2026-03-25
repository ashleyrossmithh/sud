package sud.rest.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sud.enums.CourtType;
import sud.enums.SelectItem;
import sud.enums.SelectItemImpl;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CourtCommonResource {

    @GetMapping(value = "/courtTypes/all", produces = APPLICATION_JSON_VALUE)
    public List<SelectItemImpl<Integer>> isAuthenticated() {
        return CourtType.asSelectItems();
    }
}
