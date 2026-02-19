package koscom.infra.langgraphj.core.state;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class State {
    private final ConcurrentHashMap<String, String> data;

    public State() {
        this.data = new ConcurrentHashMap<>();
    }

    public State(Map<String, String> initial) {
        this.data = new ConcurrentHashMap<>();
        if (initial != null) this.data.putAll(initial);
    }

    public String get(String key) {
        return data.get(key);
    }

    public String getOrDefault(String key, String defaultValue) {
        return data.getOrDefault(key, defaultValue);
    }

    public void put(String key, String value) {
        data.put(key, value);
    }

    public boolean containsKey(String key) {
        return data.containsKey(key);
    }

    public Map<String, String> snapshot() {
        return new ConcurrentHashMap<>(data);
    }

    @Override
    public String toString() {
        return data.toString();
    }
}