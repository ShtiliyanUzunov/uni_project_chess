package communication;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class EventBus {

    private final HashMap<String, List<Function<Object, Void>>> callbacks = new HashMap<>();

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
            callbacks.put(channel, new ArrayList<>());
        }

        callbacks.get(channel).add(callback);
    }

    public void post(String channel, Object param) {
        if (!callbacks.containsKey(channel)) {
            return;
        }

        callbacks.get(channel).forEach(x -> x.apply(param));
    }
}
