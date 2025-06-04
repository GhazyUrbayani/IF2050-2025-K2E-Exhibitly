package org.example.exhibitly.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TicketController {
    
    @FXML
    private ImageView ticketImageView;

    @FXML
    private TextField regularQuantity;

    @FXML
    private TextField premiumQuantity;

    @FXML
    private Label cartSummary;

    @FXML
    private Label totalPrice;

    private int regularTickets = 0;
    private int premiumTickets = 0;
    private final int REGULAR_PRICE = 25000;
    private final int PREMIUM_PRICE = 50000;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Image image = new Image(getClass().getResource("/images/logo.png").toExternalForm());
            ticketImageView.setImage(image);
        } catch (Exception e) {
            System.out.println("[Erorr] Couldn't load logo");
        }
        updateCartDisplay();
    }

    @FXML
    public void handleLogin(ActionEvent actionEvent) {
        try {
            navigateToPage(actionEvent, "/org/example/exhibitly/Login.fxml");
        } catch (IOException e) {
            System.err.println("[Error] " + e.getMessage());
        }
    }

    @FXML
    public void addRegularTicket(ActionEvent actionEvent) {
        try {
            int quantity = Integer.parseInt(regularQuantity.getText());

            if (quantity > 0) {
                regularTickets += quantity;
                updateCartDisplay();
            }
        } catch (NumberFormatException e) {
            System.out.println("Enter the correct number");
        }
    }

    @FXML
    public void addPremiumTicket(ActionEvent actionEvent) {
        try {
            int quantity = Integer.parseInt(premiumQuantity.getText());
            if (quantity > 0) {
                premiumTickets += quantity;
                updateCartDisplay();
            }
        } catch (NumberFormatException e) {
            System.out.println("Enter the correct number");
        }
    }

    @FXML 
    public void clearCart(ActionEvent actionEvent) {
        regularTickets = 0;
        premiumTickets = 0;
        updateCartDisplay();
    }

    @FXML
    public void proceedToCheckout(ActionEvent actionEvent) {
        if (regularTickets > 0 || premiumTickets > 0) {
            System.out.println("Proceeding to checkout...");
            System.out.println("Regular tickets: " + regularTickets);
            System.out.println("Premium tickets: " + premiumTickets);
            System.out.println("Total amount: Rp " + calculateTotal());
        } else {
            System.out.println("Cart is empty");
        }
    }

    private void updateCartDisplay() {
        if (regularTickets == 0 && premiumTickets == 0) {
            cartSummary.setText("No items in cart");
            totalPrice.setText("Total: Rp 0");
        } else {
            StringBuilder summary = new StringBuilder();
            if (regularTickets > 0) {
                summary.append(regularTickets).append(" Regular ticket");
            }
            if (premiumTickets > 0) {
                if (summary.length() > 0) summary.append(" + ");
                summary.append(premiumTickets).append(" Premium ticket");
            }

            cartSummary.setText(summary.toString());
            totalPrice.setText("Total: Rp " + String.format("%,d", calculateTotal()));
        }
    }

    private int calculateTotal() {
        return (regularTickets * REGULAR_PRICE) + (premiumTickets * PREMIUM_PRICE);
    }

    @FXML 
    public void handleExhibit(ActionEvent actionEvent) {
        try {
            navigateToPage(actionEvent, "/org/example/exhibitly/Exhibit.fxml");
        } catch (IOException e) {
            System.err.println("[Error] " + e.getMessage());
        }    
    }

    @FXML
    public void handleArtefact(ActionEvent actionEvent) {
        try {
            navigateToPage(actionEvent, "/org/example/exhibitly/artefact.fxml");
        } catch (IOException e) {
            System.err.println("Error navigating to artefact page: " + e.getMessage());
        }    
    }

    @FXML
    public void handleTickets(ActionEvent actionEvent) {
        System.out.println("Tickets button clicked!");
    }

    private void navigateToPage(ActionEvent actionEvent, String path) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(path));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1366, 768);

        try {
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("[Error] Couldn't load stylesheet");
        }

        stage.setScene(scene);
        stage.show();
    }
}
