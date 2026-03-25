package sud.core.action;


import lombok.Data;

import java.util.List;

@Data
public class AsyncActionUserRequest {
    private String question;
    private List<String> answers;
    private String defaultAnswer;
    private Long expiredTime;
    private Long waitTimeout;

    private AsyncActionUserRequest(String question, List<String> answers, String defaultAnswer, Long waitTimeout) {
        this.question = question;
        this.answers = answers;
        this.defaultAnswer = defaultAnswer;
        this.expiredTime = System.currentTimeMillis() + waitTimeout;
        this.waitTimeout = waitTimeout;
    }

    public static AsyncActionUserRequest of(String question, List<String> answers, String defaultAnswer, Long waitTimeout) {
        return new AsyncActionUserRequest(question, answers, defaultAnswer, waitTimeout);
    }

    public Long getTimeToExpire() {
        return this.expiredTime - System.currentTimeMillis();
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > this.expiredTime;
    }
}

