package sud.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String index() {
        // Убираем явное расширение или добавляем forward,
        // чтобы Spring сам нашел файл в папке static
        return "forward:/index.html";
    }
}