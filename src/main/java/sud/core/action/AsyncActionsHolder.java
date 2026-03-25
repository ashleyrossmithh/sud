package sud.core.action;


import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class AsyncActionsHolder {


    private static final ConcurrentHashMap<String, AsyncAction<?>> threadConcurrentHashMap = new ConcurrentHashMap<>();

    public static void addAction(String id, AsyncAction<?> action) { //NOSONAR
        threadConcurrentHashMap.put(id, action);
    }

    public static AsyncAction<?> getActionById(String id) {//NOSONAR
        return threadConcurrentHashMap.get(id);
    }

    public static void removeAction(String id) {
        threadConcurrentHashMap.remove(id);
    }

    @EventListener
    public void changeProgress(AsyncActionProgressEvent asyncActionProgressEvent) {
        Optional<? extends AsyncAction<?>> actionById = Optional.of(getActionById(asyncActionProgressEvent.actionThreadId()));
        actionById.ifPresent(action -> {
            action.done = asyncActionProgressEvent.done();
            if (asyncActionProgressEvent.total() > 0)
                action.total = asyncActionProgressEvent.total();
        });
    }

}
