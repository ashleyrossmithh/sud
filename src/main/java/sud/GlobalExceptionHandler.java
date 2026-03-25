package sud;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex, WebRequest request) {
        Map<String, Object> body = createErrorBody(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        Map<String, Object> body = createErrorBody(HttpStatus.UNPROCESSABLE_ENTITY, "Validation Failed");
        body.put("details", errors);
        return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        // Теперь мы ТОЧНО увидим ошибку в логах
        ex.printStackTrace();

        Map<String, Object> body = createErrorBody(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        body.put("exception_type", ex.getClass().getSimpleName());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, Object> createErrorBody(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return body;
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        Map<String, Object> body = createErrorBody(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage());
        body.put("exception_type", ex.getClass().getSimpleName());

        // Возвращаем статус 405 METHOD_NOT_ALLOWED
        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
        // Формируем тело ошибки со статусом 422
        Map<String, Object> body = createErrorBody(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        body.put("exception_type", ex.getClass().getSimpleName());

        // Если хочешь добавить детали, как мы обсуждали:
        Map<String, String> details = new HashMap<>();
        details.put("error_detail", "Нарушение бизнес-логики или пустые поля");
        body.put("details", details);

        return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}