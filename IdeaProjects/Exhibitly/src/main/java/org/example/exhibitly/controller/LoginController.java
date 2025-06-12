package org.example.exhibitly.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ProgressIndicator;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import org.example.exhibitly.models.Actor;
import org.example.exhibitly.models.Staff;
import org.example.exhibitly.models.SessionMangement;

public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label welcomeText;
    @FXML
    private Label loginMessageLabel;

    @FXML
    private Button onLoginButtonClick;
    @FXML
    private Button logoButton;
    @FXML
    private Button onLogoButtonClick;
    @FXML
    private Button loginButton;

    @FXML
    private AnchorPane rootPane;

    private StackPane loadingOverlay;
    private ProgressIndicator progressIndicator;
    private Label loadingLabel;
    private boolean istrue;

    /* -------------------------------------------------------------------------- */
    /* Initialize Page */
    /* -------------------------------------------------------------------------- */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ImageView logoHeader = (ImageView) logoButton.getGraphic();
            logoHeader.setImage(new Image(getClass().getResourceAsStream("/images/logo.png")));
        } catch (Exception e) {
            System.err.println("Error loading logo for button: " + e.getMessage());
            e.printStackTrace();
        }

        setupEnterKeyHandlers();
        setupLoadingOverlay();
    }

    /* -------------------------------------------------------------------------- */
    /* Start Setup Logics */
    /* -------------------------------------------------------------------------- */

    private void setupLoadingOverlay() {
        loadingOverlay = new StackPane();
        loadingOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        loadingOverlay.setVisible(false);

        progressIndicator = new ProgressIndicator();
        progressIndicator.setMaxSize(50, 50);
        progressIndicator.setStyle("-fx-accent: white");

        loadingLabel = new Label("Authenticating...");
        loadingLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        javafx.scene.layout.VBox loadingContainer = new javafx.scene.layout.VBox(10);
        loadingContainer.setAlignment(javafx.geometry.Pos.CENTER);
        loadingContainer.getChildren().addAll(progressIndicator, loadingLabel);

        loadingOverlay.getChildren().add(loadingContainer);

        try {
            Platform.runLater(() -> {
                if (usernameField.getScene() != null && usernameField.getScene().getRoot() instanceof AnchorPane) {
                    AnchorPane root = (AnchorPane) usernameField.getScene().getRoot();
                    root.getChildren().add(loadingOverlay);

                    AnchorPane.setTopAnchor(loadingOverlay, 0.0);
                    AnchorPane.setBottomAnchor(loadingOverlay, 0.0);
                    AnchorPane.setLeftAnchor(loadingOverlay, 0.0);
                    AnchorPane.setRightAnchor(loadingOverlay, 0.0);
                }
            });
        } catch (Exception e) {
            System.err.println("Error setting up loading overlay: " + e.getMessage());
        }
    }

    /* -------------------------------------------------------------------------- */
    /* Overlay Logics */
    /* -------------------------------------------------------------------------- */

    private void showLoadingOverlay() {
        Platform.runLater(() -> {
            if (loadingOverlay != null) {
                loadingOverlay.setVisible(true);
                loadingOverlay.toFront();
            }
        });
    }

    private void hideLoadingOverlay() {
        Platform.runLater(() -> {
            if (loadingOverlay != null) {
                loadingOverlay.setVisible(false);
            }
        });
    }

    /* -------------------------------------------------------------------------- */
    /* Key Handler Logics */
    /* -------------------------------------------------------------------------- */

    private void setupEnterKeyHandlers() {
        usernameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLogin();
            }
        });

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLogin();
            }
        });
    }

    /* -------------------------------------------------------------------------- */
    /* Login Logics */
    /* -------------------------------------------------------------------------- */

    @FXML
    protected void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isBlank() || password.isBlank()) {
            loginMessageLabel.setText("Username dan Password tidak boleh kosong.");
            return;
        }

        if (!username.isBlank() && !password.isBlank()) {
            System.out.println("Mencoba login!");

            showLoadingOverlay();

            usernameField.setDisable(true);
            passwordField.setDisable(true);
            onLoginButtonClick.setDisable(true);

            Task<Actor> loginTask = new Task<Actor>() {
                @Override
                protected Actor call() throws Exception {
                    Thread.sleep(1000);
                    return validateLogin(username, password);
                }

                @Override
                protected void succeeded() {
                    Platform.runLater(() -> {
                        hideLoadingOverlay();

                        usernameField.setDisable(false);
                        passwordField.setDisable(false);
                        onLoginButtonClick.setDisable(false);

                        Actor user = getValue();
                        if (user != null) {
                            SessionMangement.getSession().login(user);
                            System.out.println("Login successful! Redirecting...");
                            navigateBasedOnRole(user);
                        } else {
                            loginMessageLabel.setText("Login gagal! Silahkan coba lagi.");
                            System.out.println("Login failed! Please check credentials.");
                            // TODO: Show error message to user

                            passwordField.clear();
                            usernameField.requestFocus();
                        }
                    });
                }

                @Override
                protected void failed() {
                    Platform.runLater(() -> {
                        hideLoadingOverlay();
                        usernameField.setDisable(false);
                        passwordField.setDisable(false);
                        onLoginButtonClick.setDisable(false);

                    });
                }
            };

            Thread loginThread = new Thread(loginTask);
            loginThread.setDaemon(true);
            loginThread.start();
        } else {
            loginMessageLabel.setText("Username atau Password salah.");
        }
    }

    private Actor validateLogin(String username, String password) {
        String sql = "SELECT actorID, username, password, name, role FROM actor WHERE username = ? AND password = ?";
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();
            PreparedStatement preparedStatement = connectDB.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int actorID = resultSet.getInt("actorID");
                String dbUsername = resultSet.getString("username");
                String dbPassword = resultSet.getString("password");
                String name = resultSet.getString("name");
                String role = resultSet.getString("role");
                Actor user;

                if (role.equalsIgnoreCase("Staff")) {
                    String staffSql = "SELECT jadwalPemeliharaan FROM staffpemeliharaan WHERE actorID = ?";
                    PreparedStatement staffStmt = connectDB.prepareStatement(staffSql);
                    staffStmt.setInt(1, actorID);
                    ResultSet staffResult = staffStmt.executeQuery();

                    String jadwal = staffResult.next() ? staffResult.getString("jadwalPemeliharaan") : "Not set";
                    user = new Staff(actorID, dbUsername, dbPassword, name, jadwal);
                    staffResult.close();
                    staffStmt.close();
                } else {
                    user = new Actor(actorID, dbUsername, dbPassword, name, role);
                }
                resultSet.close();
                preparedStatement.close();
                connectDB.close();

                return user;
            }
            connectDB.close();
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        return null;
    }

    @FXML
    private void onLoginButtonClick(ActionEvent event) {
        System.out.println("Sudah ada di login page");
    }

    @FXML
    private void onExhibitButtonClick(ActionEvent event) {
        navigateToPage(event, "/org/example/exhibitly/Exhibit.fxml");
    }

    @FXML
    private void onArtefactButtonClick(ActionEvent event) {
        navigateToPage(event, "/org/example/exhibitly/Artefact.fxml");

    }

    @FXML
    private void onTicketButtonClick(ActionEvent event) {
        navigateToPage(event, "/org/example/exhibitly/Ticket.fxml");
    }

    @FXML
    private void onLogoButtonClick(ActionEvent event) {
        navigateToPage(event, "/org/example/exhibitly/LandingPage.fxml");
    }

    private void navigateBasedOnRole(Actor user) {
        String path;
        String title;

        switch (user.getRole().toLowerCase()) {
            case "staff":
                path = "/org/example/exhibitly/LandingPage.fxml";
                title = "Museum Nusantara - Dashboard";
                break;

            default:
                path = "/org/example/exhibitly/LandingPage.fxml";
                title = "Museum Nusantara - Dashboard";
                break;
        }

        navigateToPage(path, title);
    }

    private void navigateToPage(ActionEvent event, String path) {
        String pageName = path.substring(path.lastIndexOf('/') + 1).replace(".fxml", "");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root, 1366, 768);
            stage.setScene(scene);

            stage.setTitle("Museum Nusantara - " + pageName);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat halaman " + pageName + ": " + e.getMessage());
        }
    }

    private void navigateToPage(String path, String title) {
        try {
            Stage currentStage = (Stage) usernameField.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent root = loader.load();

            Scene scene = new Scene(root, 1366, 768);
            currentStage.setScene(scene);

            currentStage.setTitle(title);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to navigate to: " + title + " - " + e.getMessage());
        }
    }
}
