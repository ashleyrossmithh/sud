package sud.core.action.checkutils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Check implements Serializable {

    private LocalDateTime time;
    private CheckSeverity checkSeverity;
    private String messageText;

    public Check(CheckSeverity checkSeverity, String messageText) {
        this.checkSeverity = checkSeverity;
        this.messageText = messageText;
    }

    public static Check criticalError(String messageText) {
        Check check = new Check();
        check.setCheckSeverity(CheckSeverity.CRITICAL_ERROR);
        check.setMessageText(messageText);
        return check;
    }

    public static Check info(String messageText) {
        Check check = new Check();
        check.setCheckSeverity(CheckSeverity.INFO);
        check.setMessageText(messageText);
        return check;
    }

    public static Check error(String messageText) {
        Check check = new Check();
        check.setCheckSeverity(CheckSeverity.ERROR);
        check.setMessageText(messageText);
        return check;
    }

    public static Check warning(String messageText) {
        Check check = new Check();
        check.setCheckSeverity(CheckSeverity.WARNING);
        check.setMessageText(messageText);
        return check;
    }

}
