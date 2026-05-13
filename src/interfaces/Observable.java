package interfaces;

public interface Observable {
    void addObserver(Observer observer);

    void notifyObservers(String event);
}
