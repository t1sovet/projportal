package utils;

import java.io.Serializable;
import java.util.List;
import interfaces.Observer;

public class SystemLogger implements Observer, Serializable {
    private final static List<String> logs = new java.util.ArrayList<>();

    @Override
    public void onEvent(String event) {
        logs.add(java.time.LocalDateTime.now() + ": " + event);
        System.out.println("System Log: " + event);
    }

    public List<String> getLogs() {
        return logs;
    }
}
