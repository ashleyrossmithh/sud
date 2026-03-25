package sud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // Она сама просканирует всё внутри пакета sud
public class SudApplication {
    public static void main(String[] args) {
        SpringApplication.run(SudApplication.class, args);
    }
}