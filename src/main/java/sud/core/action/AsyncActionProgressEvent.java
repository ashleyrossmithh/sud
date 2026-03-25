package sud.core.action;


public record AsyncActionProgressEvent(String actionThreadId, int done, int total) {
}
