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

public class TicketController implements Initializable {
    
    
    @FXML
    private ImageView ticketHeaderImage;

    @FXML
    private ImageView logoFooter;

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
            ticketHeaderImage.setImage(new Image(getClass().getResourceAsStream("/images/logo.png")));
            logoFooter.setImage(new Image(getClass().getResourceAsStream("/images/logo2.png")));
        } catch (Exception e) {
            System.out.println("[Erorr] Couldn't load logo");
        }
        updateCartDisplay();
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
    private void onLoginButtonClick(ActionEvent actionEvent) {
        navigateToPage(actionEvent, "/org/example/exhibitly/login.fxml");    
    }
    
    @FXML
    private void onExhibitButtonClick(ActionEvent actionEvent) {
        navigateToPage(actionEvent, "/org/example/exhibitly/Exhibit.fxml");
    }

    @FXML
    private void onArtefactButtonClick(ActionEvent actionEvent) {
        navigateToPage(actionEvent, "/org/example/exhibitly/Artefact.fxml");

    }

    @FXML
    private void onTicketButtonClick(ActionEvent actionEvent) {
        System.out.println("Sudah ada di Ticket Page!");
    }

    @FXML
    private void onLogoButtonClick(ActionEvent actionEvent) {
        navigateToPage(actionEvent, "/org/example/exhibitly/LandingPage.fxml");
    }

    private void navigateToPage(ActionEvent actionEvent, String path) {
        String pageName = path.substring(path.lastIndexOf('/') + 1).replace(".fxml", "");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            
            stage.setTitle("Museum Nusantar - " + pageName);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat halaman " + pageName + ": " + e.getMessage());
        }
    }
}
