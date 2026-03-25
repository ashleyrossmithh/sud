package sud.core.rest.action;

import sud.core.action.AsyncActionStatus;
import sud.core.action.AsyncActionUserAnswer;
import sud.core.action.AsyncActionsHolder;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Log4j2
@RestController
@RequestMapping("/api/asyncAction")
public class AsyncActionResource {

    @GetMapping(value = "/status/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AsyncActionStatus<?>> checkStatus(@PathVariable String id) { //NOSONAR
        return ResponseEntity.ok(AsyncActionsHolder.getActionById(id));
    }


    @PostMapping(value = "/answer")
    public ResponseEntity<Void> sendAnswer(@RequestBody AsyncActionUserAnswer asyncActionUserAnswer) {
        log.info("sendAnswer" + asyncActionUserAnswer);
        AsyncActionsHolder.getActionById(asyncActionUserAnswer.getAsyncActionId())
                .processUserAnswer(asyncActionUserAnswer.getAnswer());
        return ResponseEntity.ok().build();
    }
}
