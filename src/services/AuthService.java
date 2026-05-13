package services;

import exceptions.AuthenticationException;
import interfaces.Observable;
import interfaces.Observer;
import models.User;

import java.util.List;
import java.util.ArrayList;

public class AuthService implements Observable {
    private final UniversityDatabase db;
    private User currentUser;
    private final List<Observer> observers = new ArrayList<>();

    public AuthService(UniversityDatabase db) {
        this.db = db;
    }

    public User login(String email, String password) throws AuthenticationException {
        for (User user : db.getUsers()) {
            if (user.getEmail().equals(email) && user.checkPassword(password)) {
                if (user.getEmail().equals(email)) {
                    System.out.println("Email matched for user: " + user.getName());
                }
                currentUser = user;
                notifyObservers("SUCCESSFUL_LOGIN: " + email);
                return user;
            }
        }
        notifyObservers("FAILED_LOGIN_ATTEMPT: " + email);
        throw new AuthenticationException("Invalid email or password");
    }

    public void logout() {
        if (currentUser != null) {
            notifyObservers("LOGOUT: " + currentUser.getEmail());
            currentUser = null;
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers(String event) {
        for (Observer observer : observers) {
            observer.onEvent(event);
        }
    }
}
