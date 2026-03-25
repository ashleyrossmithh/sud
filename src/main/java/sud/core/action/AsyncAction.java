package sud.core.action;

import sud.core.action.checkutils.Check;
import sud.core.action.checkutils.CheckInfo;
import sud.core.action.checkutils.CheckSeverity;
import sud.core.text.Message;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
public abstract class AsyncAction<T> implements AsyncActionStatus<T> {


    private static final ConcurrentHashMap<Long, CopyOnWriteArrayList<String>> sessionsActions = new ConcurrentHashMap<>();
    @Getter
    protected final String id;
    private final Thread thread;
    private final String name;
    @Getter
    private final CheckInfo checkInfo = new CheckInfo();
    private final Map<String, String> contextMap = MDC.getCopyOfContextMap();
    private final AtomicReference<AsyncActionUserRequest> currentUserRequest = new AtomicReference<>(null);
    private final Exchanger<String> exchanger = new Exchanger<>();
    @Setter
    private List<AsyncActionPostAction> asyncActionPostActions = new ArrayList<>();
    protected String resultFilename;
    protected String downloadFileName;
    protected String logFileName;

    protected boolean automaticFileDownload = true;

    @Setter
    protected T data;
    protected int done = 0;
    protected int total = 1;
    protected LocalDateTime startDateTime;
    protected LocalDateTime finishDateTime;
//    protected ReportRunModeEnum reportRunModeEnum = ReportRunModeEnum.BROWSER;
    @Setter
    private boolean showProgress = false;
    @Getter
    private Boolean isError;
    @Getter
    private String errorMessage;


    protected AsyncAction(String name) {
        this.thread = new Thread(() -> {
            try {
                log.info(Message.format("{0} action started. name = {1} ", this.getClass().toString(), name));
                if (contextMap != null)
                    MDC.setContextMap(contextMap);
                startDateTime = LocalDateTime.now();
                this.run();
                finishDateTime = LocalDateTime.now();
                this.onThreadFinished();
                processPostActions();
            } catch (Exception e) {
                log.error(Message.format("{0} action failed. Name = {1} ", this.getClass().toString(), name), e);
            }

        });
        this.thread.setName(name);
        this.name = name;
        this.id = UUID.randomUUID().toString();
        AsyncActionsHolder.addAction(id, this);
//        addToSessions(id);
        this.isError = false;
    }

    @SuppressWarnings("unused")
    public static void onSessionClose(Long sessionId) {

        CopyOnWriteArrayList<String> sessionActions = sessionsActions.remove(sessionId);
        if (sessionActions == null)
            return;
        sessionActions.forEach(s -> {
            log.trace(() -> "Удаляем данные по процессу ".concat(s));
            AsyncActionsHolder.removeAction(s);

        });

    }

    private void processPostActions() {
        asyncActionPostActions.stream()
/*                .filter(asyncActionPostAction ->
                        asyncActionPostAction.getReportRunModeEnum().equals(reportRunModeEnum))*/
                .forEach(asyncActionPostAction -> asyncActionPostAction.process(this));

    }

    private void addToSessions(String id) {
//        IUserInfoCore userInfo = IUserInfoCore.getUserInfo();
//        if (userInfo == null)
//            return;
//        Long sessionId = Objects.requireNonNull(userInfo).getSessionId();
//        if (sessionId == null)
//            return;
//        CopyOnWriteArrayList<String> sessionActions = sessionsActions.get(sessionId);
//        if (sessionActions == null)
//            sessionActions = new CopyOnWriteArrayList<>();
//        sessionActions.add(id);
//        sessionsActions.put(sessionId, sessionActions);
    }

    protected void onThreadFinished() {
        log.trace(() -> name.concat(" Finished"));
    }

    public abstract void run();

    public String start() {
        this.thread.start();
        return id;
    }

    public Boolean getShowProgress() {
        return this.showProgress;
    }

    public String getLabel() {
        return name;
    }

    @Override
    public Boolean getIsActive() {
        return this.thread.isAlive();
    }

    @SuppressWarnings("unused")
    protected void addToChecks(Collection<Check> checks) {
        checks.forEach(this::addToChecks);
    }

    protected void addToChecks(Check check) {
        log.trace(Message.format("add to check {0}", check.getMessageText()));
        if (check.getCheckSeverity().equals(CheckSeverity.CRITICAL_ERROR)) {
            criticalError(check.getMessageText());
            return;
        }
        this.checkInfo.addCheck(check);
    }

    protected void criticalError(String message) {
        this.isError = true;
        this.errorMessage = message;
        checkInfo.addCriticalError(message);
    }

    @Override
    public Integer getProgress() {
        return done * 100 / total;
    }

    @Override
    public String getStatusDesc() {
        return Message.format("обработано {0} записей из {1}", done, total);
    }

    @Override
    public String getResultFileName() {
        return resultFilename;
    }

    @Override
    public String getDownloadFileName() {
        return downloadFileName;
    }

    @Override
    public String getLogFileName() {
        return logFileName;
    }

    @Override
    public boolean isAutomaticFileDownload() {
        return automaticFileDownload;
    }
    @Override
    public T getData() {
        return data;
    }

    @Override
    public AsyncActionUserRequest getWaitUserAnswer() {
        return this.currentUserRequest.get();
    }

    public final void processUserAnswer(String answer) {
        log.info("Ответ от пользователя: " + answer);

        try {
            if (currentUserRequest.getAndSet(null) != null) {
                exchanger.exchange(answer, 3, TimeUnit.SECONDS);
            }
        } catch (InterruptedException | TimeoutException e) {
            Thread.currentThread().interrupt();
        }
    }


    @SuppressWarnings("unused")
    public final String waitForUserAnswer(AsyncActionUserRequest asyncActionUserRequest) { //NOSONAR
        log.info("Ожидаем ответа от пользователя " + asyncActionUserRequest);
        this.currentUserRequest.set(asyncActionUserRequest);
        try {
            String answer = exchanger.exchange(null, asyncActionUserRequest.getWaitTimeout(), TimeUnit.MILLISECONDS);
            log.info("Ответ: " + answer);
            return answer;
        } catch (InterruptedException | TimeoutException e) {
            currentUserRequest.set(null);
            Thread.currentThread().interrupt();
            return asyncActionUserRequest.getDefaultAnswer();
        }
    }

    public String getDurationInfo() {
        if (startDateTime == null || finishDateTime == null)
            return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        LocalDateTime tempDateTime = LocalDateTime.from(startDateTime);

        long hours = tempDateTime.until(finishDateTime, ChronoUnit.HOURS);
        tempDateTime = tempDateTime.plusHours(hours);

        long minutes = tempDateTime.until(finishDateTime, ChronoUnit.MINUTES);
        tempDateTime = tempDateTime.plusMinutes(minutes);

        long seconds = tempDateTime.until(finishDateTime, ChronoUnit.SECONDS);

        String duration = hours + " ч :  " +
                minutes + " м : " +
                seconds + " c ";

        return "формирование: " + formatter.format(startDateTime) + " по " + formatter.format(finishDateTime) + " \n" + duration;
    }


}
