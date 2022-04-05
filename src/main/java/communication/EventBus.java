package communication;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Function;

public class EventBus {

    private final ConcurrentHashMap<String, Vector<Function<Object, Void>>> callbacks = new ConcurrentHashMap();

    private Set<String> threadAccess = new CopyOnWriteArraySet<>();

    private static EventBus instance;

    private EventBus() {
    }

    public static EventBus getEventBus() {
        if (instance == null) {
            instance = new EventBus();
        }

        return instance;
    }

    public void register(String channel, Function<Object, Void> callback) {
        if (!callbacks.containsKey(channel)) {
            callbacks.put(channel, new Vector<>());
        }

        callbacks.get(channel).add(callback);
    }

    public void post(String channel, Object param) {
//        Concurrency exception debugging
//        String threadName = Thread.currentThread().getName();
//        threadAccess.add(Thread.currentThread().getName());
//
//        if (threadName.equalsIgnoreCase("main")) {
//            threadName = threadName;
//        }
//        if (threadName.equalsIgnoreCase("pool-1-thread-1")) {
//            threadName = threadName;
//        }

        if (!callbacks.containsKey(channel)) {
            return;
        }

        callbacks.get(channel).forEach(x -> x.apply(param));

    }
}
