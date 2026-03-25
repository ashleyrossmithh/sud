package sud.rest.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import sud.core.action.checkutils.Check;
import sud.domain.person.PersonDTOService;
import sud.domain.person.PersonRepository;
import sud.domain.user_data.UserData;
import sud.domain.user_data.UserDataDTO;
import sud.domain.user_data.UserDataRepository;
import sud.domain.user_data.UserDataService;

import java.net.URISyntaxException;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api/user_data")
@RequiredArgsConstructor
@Log4j2
public class UserDataResource {
    private final UserDataRepository userDataRepository;
    private final PersonDTOService personDTOService;
    private final UserDataService userDataService;
    private final PasswordEncoder passwordEncoder;

    @RequestMapping(
            value = {"/create"},
            method = {RequestMethod.POST},
            produces = {"application/json"}
    )
    public Check create(@RequestBody UserDataDTO userDataDTO) throws URISyntaxException {
        if (userDataDTO.getPasswordNew() == null) {
            userDataService.createUserData(userDataDTO.getLogin(), userDataDTO.getPassword(), userDataDTO.getPerson() == null ? null : userDataDTO.getPerson().getId());
        } else {
            UserData user = userDataRepository.findByLogin(userDataDTO.getLogin());
            String oldPwd = userDataDTO.getPassword();
            if (!passwordEncoder.matches(oldPwd, user.getPassword())) {
                return Check.error("Неверный пароль");
            }
            user.setPassword(passwordEncoder.encode(userDataDTO.getPasswordNew()));
            userDataRepository.save(user);
        }
        return null;
    }

    @RequestMapping(value = "/{login:.+}", method = DELETE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String login) {
        log.debug("Delete by id Court : {}", login);
        userDataService.clearPassword(login);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/loadUsers/all", method = GET, produces = APPLICATION_JSON_VALUE)
    public List<UserDataDTO> findAll() throws URISyntaxException {
        return userDataRepository.findAll().stream().map(curUser ->
                UserDataDTO.builder()
                        .login(curUser.getLogin())
                        .person(personDTOService.toDTO(curUser.getPerson(), 1))
                        .build()
        ).toList();
    }

    @RequestMapping(value = "/check_create/{login}", method = GET, produces = APPLICATION_JSON_VALUE)
    public Check checkCreate(@PathVariable String login) {
        if(userDataRepository.findByLogin(login) != null) {
            return Check.error(String .format("Невозможно созать пользователя. Пользователь с логином \"%s\" уже существует", login));
        }
        return null;
    }


}
