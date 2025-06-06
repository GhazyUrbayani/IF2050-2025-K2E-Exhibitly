package org.example.exhibitly.models;

public class SessionMangement {
    private static SessionMangement session;
    private Actor currentActor;
    private boolean isLoggedIn;
 
    private SessionMangement() { //Private karena mengimplementasikan Singleton
        this.currentActor = null;
        this.isLoggedIn = false;
    }

    public static SessionMangement getSession() {
        if (session == null) {
            session = new SessionMangement();
        }
        return session;
    }

    public void login(Actor actor) {
        this.currentActor = actor;
        this.isLoggedIn = true;
    }
    
    public void logout() {
        this.currentActor = null;
        this.isLoggedIn = false;
    }

    public Actor getCurrentActor() {
        return currentActor;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public boolean isStaff() {
        return isLoggedIn && currentActor != null && currentActor.getRole().equalsIgnoreCase("Staff");
    }

    public boolean isKurator() {
        return isLoggedIn && currentActor != null && currentActor.getRole().equalsIgnoreCase("Kurator");
    }

    public boolean hasPermission(String permission) {
        if (!isLoggedIn || currentActor == null) {
            return false;
        }

        switch (permission) {
            case "maintenance":
                return currentActor.getRole().equalsIgnoreCase("Staff");
            
            //TODO: tambahkan Role!

            default:
                return false;
        }
    }
}
