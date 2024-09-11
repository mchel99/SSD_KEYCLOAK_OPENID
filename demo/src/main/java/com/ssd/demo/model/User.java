package com.ssd.demo.model;

public class User {

    // Attributi privati
    private String username;
    private String password;

    // Costruttore vuoto
    public User() {
    }

    // Costruttore con parametri
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter per username
    public String getUsername() {
        return username;
    }

    // Setter per username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter per password
    public String getPassword() {
        return password;
    }

    // Setter per password
    public void setPassword(String password) {
        this.password = password;
    }

    // Metodo toString per una rappresentazione testuale dell'oggetto
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
