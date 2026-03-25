package sud.core.action;

public interface AsyncActionPostAction {
    // ReportRunModeEnum getReportRunModeEnum();

    void process(AsyncAction<?> asyncAction);
}
