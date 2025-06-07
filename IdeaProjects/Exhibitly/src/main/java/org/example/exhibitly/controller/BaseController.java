package org.example.exhibitly.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.example.exhibitly.models.SessionMangement;

import java.io.IOException;
import java.util.Optional;

public abstract class BaseController {
    
    protected SessionMangement session = SessionMangement.getSession();
    
    protected void navigateToPage(ActionEvent event, String path) {
        navigateToPage(event, path, false);
    }


    protected void navigateToPage(ActionEvent event, String path, boolean requireLogin) {
        if (requireLogin && !session.isLoggedIn()) {
            showLoginRequired();
            navigateToLogin(event);
            return;
        }
        
        String pageName = path.substring(path.lastIndexOf('/') + 1).replace(".fxml", "");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Casting ke Node diperlukan

            // Buat Scene dengan ukuran tetap 1366x768
            Scene scene = new Scene(root, 1366, 768);
            stage.setScene(scene);

            // Set judul Stage secara konsisten
            stage.setTitle("Museum Nusantara - " + pageName);
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load " + pageName + ": " + e.getMessage());
        }
    }
    
    protected void navigateToStaffPage(ActionEvent event, String fxmlPath) {
        if (!session.isLoggedIn()) {
            showLoginRequired();
            navigateToLogin(event);
            return;
        }
        
        if (!session.isStaff()) {
            showInsufficientPermissions();
            return;
        }
        
        navigateToPage(event, fxmlPath, false);
    }
    
    protected void handleLogout(ActionEvent event) {
        if (session.isLoggedIn()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout Confirmation");
            alert.setHeaderText("Are you sure you want to logout?");
            alert.setContentText("You will be redirected to the login page.");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                session.logout();
                navigateToLogin(event);
            }
        } else {
            navigateToLogin(event);
        }
    }
    
    private void navigateToLogin(ActionEvent event) {
        navigateToPage(event, "/org/example/exhibitly/login.fxml", false);
    }
    
    private void showLoginRequired() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Login Required");
        alert.setHeaderText("You must be logged in to access this page");
        alert.setContentText("Please login to continue.");
        alert.showAndWait();
    }
    
    private void showInsufficientPermissions() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Access Denied");
        alert.setHeaderText("Insufficient Permissions");
        alert.setContentText("You don't have permission to access this page.");
        alert.showAndWait();
    }
    
    protected String getCurrentUserDisplay() {
        if (session.isLoggedIn()) {
            return session.getCurrentActor() + " (" + session.getCurrentActor().getRole() + ")";
        }
        return "Guest";
    }
    
    protected boolean isUserLoggedIn() {
        return session.isLoggedIn();
    }
    
    protected boolean isCurrentUserStaff() {
        return session.isStaff();
    }
}