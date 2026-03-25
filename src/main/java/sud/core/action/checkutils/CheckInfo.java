package sud.core.action.checkutils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings("unused")
public class CheckInfo implements Serializable {
    private List<Check> checks = new ArrayList<>();
    private String dialogTitle;

    public CheckInfo(List<Check> checks) {
        this.checks = checks;
    }

    public boolean isEmpty() {
        return checks == null || checks.isEmpty();
    }

    public boolean containsCritical() {
        return contains(check -> check.getCheckSeverity().equals(CheckSeverity.CRITICAL_ERROR));
    }

    public boolean containsAnyErrors() {
        return contains(check -> check.getCheckSeverity().equals(CheckSeverity.CRITICAL_ERROR)
                || check.getCheckSeverity().equals(CheckSeverity.ERROR));
    }

    public boolean contains(Predicate<? super Check> predicate) {
        return checks != null && !checks.isEmpty() && checks.stream().anyMatch(predicate);
    }

    public void addCriticalError(String messageText) {
        Check check = new Check();
        check.setCheckSeverity(CheckSeverity.CRITICAL_ERROR);
        check.setMessageText(messageText);
        this.checks.add(check);
    }

    public void addInfo(String messageText) {
        Check check = new Check();
        check.setCheckSeverity(CheckSeverity.INFO);
        check.setMessageText(messageText);
        this.checks.add(check);
    }

    public void addError(String messageText) {
        Check check = new Check();
        check.setCheckSeverity(CheckSeverity.ERROR);
        check.setMessageText(messageText);
        this.checks.add(check);
    }

    public void addCheck(CheckSeverity checkSeverity, String messageText) {
        Check check = new Check();
        check.setCheckSeverity(checkSeverity);
        check.setMessageText(messageText);
        this.checks.add(check);
    }

    public void addCheck(Check check) {
        this.checks.add(check);
    }

    public void clear() {
        this.checks.clear();
    }
}
