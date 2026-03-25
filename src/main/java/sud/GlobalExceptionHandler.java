package sud;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 1. Ошибка 404 (Суд не найден)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    // 2. Ошибка 405 (Не тот метод, например POST вместо GET)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage(), request);
    }

    // 3. Ошибка 422 (Ошибки валидации или бизнес-логики)
    @ExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class})
    public ResponseEntity<?> handleValidationAndLogic(Exception ex, WebRequest request) {
        String message = (ex instanceof MethodArgumentNotValidException) ? "Ошибка валидации данных" : ex.getMessage();
        return buildErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, message, request);
    }

    // 4. Глобальная ошибка 500 (Если всё сломалось)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        ex.printStackTrace(); // Чтобы видеть причину в консоли
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера: " + ex.getMessage(), request);
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message, WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("code", status.value());
        errorDetails.put("message", message);

        // Достаем чистый путь запроса (например, /api/entities/court/132)
        String path = request.getDescription(false).replace("uri=", "");
        errorDetails.put("path", path);

        Map<String, Object> body = new HashMap<>();
        body.put("error", errorDetails);

        return new ResponseEntity<>(body, status);
    }
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleBadRequest(org.springframework.http.converter.HttpMessageNotReadableException ex, WebRequest request) {
        // Вытаскиваем только суть ошибки, без лишнего мусора
        String cleanMessage = "Ошибка синтаксиса JSON: проверьте запятые и кавычки";
        return buildErrorResponse(HttpStatus.BAD_REQUEST, cleanMessage, request);
    }
}