package sud.core.action;


import sud.core.action.checkutils.CheckInfo;

public interface AsyncActionStatus<T> {

    Boolean getShowProgress();

    Integer getProgress();

    String getStatusDesc();

    String getLabel();

    Boolean getIsActive();

    Boolean getIsError();

    String getErrorMessage();

    CheckInfo getCheckInfo();

    String getResultFileName();

    String getDownloadFileName();

    String getLogFileName();
    boolean isAutomaticFileDownload();

    T getData();

    AsyncActionUserRequest getWaitUserAnswer();
}
