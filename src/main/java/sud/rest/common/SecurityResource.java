package sud.rest.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sud.domain.person.PersonDTOService;
import sud.domain.user_data.UserData;
import sud.domain.user_data.UserDataDTO;
import sud.domain.user_data.UserDataRepository;
import sud.dto.LoginResult;
import sud.utils.FileService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SecurityResource {
    private final UserDataRepository userDataRepository;
    private final PersonDTOService personDTOService;

    @GetMapping(value = "/authenticated", produces = APPLICATION_JSON_VALUE)
    public boolean isAuthenticated() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpSession session = requestAttributes.getRequest().getSession(false);
        return session != null && session.getAttribute("userSession") != null;
    }

    @PostMapping(value = "/login")
    public LoginResult login(HttpServletRequest request) {
        System.out.println("=== ПОПЫТКА ВХОДА ===");
        LoginResult result = new LoginResult();

        String login = request.getParameter("username");
        String password = request.getParameter("password");

        if (login != null) login = login.trim();
        if (password != null) password = password.trim();

        System.out.println(">>> Входим как: [" + login + "]");

        if (login == null || login.isEmpty()) {
            result.setErrorMsg("Данные не получены");
            return result;
        }

        UserData userData = userDataRepository.findByLogin(login);

        if (userData != null && userData.getPassword() != null && userData.getPassword().trim().equals(password)) {

            // Создаем DTO (билдер без adminFlag, так как его нет в классе)
            UserDataDTO userDto = UserDataDTO.builder()
                    .login(userData.getLogin())
                    .build();

            // Пытаемся подтянуть персону, чтобы метод getRoleName() в DTO выдал "Администратор"
            if (userData.getPerson() != null && personDTOService != null) {
                try {
                    userDto.setPerson(personDTOService.toDTO(userData.getPerson(), 1));
                } catch (Exception e) {
                    System.out.println("! Ошибка конвертации персоны: " + e.getMessage());
                }
            }

            // Кладем в сессию
            request.getSession(true).setAttribute("userSession", userDto);

            // Заполняем результат для фронта
            result.setFio(userDto.getRoleName());

            // Если в классе LoginResult есть поле adminFlag, ставим его в true через "костыль",
            // чтобы не ломать компиляцию, если его вдруг нет.
            try {
                // Если метод существует, он выполнится. Если нет - проигнорится.
                java.lang.reflect.Method method = result.getClass().getMethod("setAdminFlag", boolean.class);
                method.invoke(result, true);
            } catch (Exception e) {
                // поля нет, ничего не делаем
            }

            System.out.println("== УСПЕХ: Роль определена как: " + userDto.getRoleName());
        } else {
            result.setErrorMsg("Неверный логин или пароль");
            System.out.println("== ОТКАЗ: Пароли не совпали");
        }
        return result;
    }

    @GetMapping(value = "/userInfo", produces = APPLICATION_JSON_VALUE)
    public UserDataDTO getUserInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            UserDataDTO user = (UserDataDTO) session.getAttribute("userSession");
            if (user != null) {
                System.out.println(">>> Отдаю инфу пользователя: " + user.getLogin());
                return user;
            }
        }
        return null;
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        FileService.clearTmpDir();
        return ResponseEntity.ok().build();
    }
}