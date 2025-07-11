package org.example.exhibitly.models;

public class Actor {
    private int actorID;
    private String username;
    private String password;
    private String name;
    private String role;

    public Actor() {
    }

    public Actor(int actorID, String username, String password, String name, String role) {
        this.actorID = actorID;
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public int getActorID() {
        return actorID;
    }

    public void setActorID(int actorID) {
        this.actorID = actorID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}