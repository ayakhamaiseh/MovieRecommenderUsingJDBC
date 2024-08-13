package org.example;

public class User {
    private final int userId;
    private final String username;
    public User(int userId, String username) {
        this.userId = userId;
        this.username = username;
    }
    public int getId() {
        return userId;
    }
    public String getUsername() {
        return username;
    }
}

