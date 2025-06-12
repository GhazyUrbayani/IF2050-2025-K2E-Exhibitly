package org.example.exhibitly.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;

import org.example.exhibitly.models.Ticket;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.UUID;

public class TicketsController extends BaseController implements Initializable {

    @FXML
    private ImageView logoFooter;
    @FXML
    private Button logoButton;
    @FXML
    private ImageView qrCodeImageView;
    @FXML
    private Label ticketNumberLabel;

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField quantityField;

    private int ticketQuantity = 1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ImageView logoImageView = (ImageView) logoButton.getGraphic();
            logoImageView.setImage(new Image(getClass().getResourceAsStream("/images/logo.png")));
        } catch (Exception e) {
            System.err.println("Error loading logo for button: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            logoFooter.setImage(new Image(getClass().getResourceAsStream("/images/logo2.png")));
        } catch (Exception e) {
            System.out.println("[Erorr] Couldn't load logo");
        }

        quantityField.setText(String.valueOf(ticketQuantity));

    }

    @FXML
    private void onMinusButtonClick(ActionEvent event) {
        if (ticketQuantity > 1) {
            ticketQuantity--;
            quantityField.setText(String.valueOf(ticketQuantity));
        }
    }

    @FXML
    private void onPlusButtonClick(ActionEvent event) {
        ticketQuantity++;
        quantityField.setText(String.valueOf(ticketQuantity));
    }

    @FXML
    private void onBuyButtonClick(ActionEvent event) {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || ticketQuantity <= 0) {
            showAlert(Alert.AlertType.WARNING, "Input Tidak Lengkap",
                    "Nama, Email, dan Jumlah Tiket harus diisi dan valid.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Email Tidak Valid",
                    "Format email tidak valid. Email harus mengandung '@', '.', dan domain yang benar (misal: user@domain.com).");
            return;
        }

        String ticketId = UUID.randomUUID().toString().substring(0, 10).toUpperCase();
        String qrCodeData = String.format("Ticket ID: %s\nName: %s\nEmail: %s\nQuantity: %d",
                ticketId, name, email, ticketQuantity);

        try {
            BufferedImage qrImage = generateQRCodeImage(qrCodeData, 200, 200);
            Image fxImage = SwingFXUtils.toFXImage(qrImage, null);
            qrCodeImageView.setImage(fxImage);
            ticketNumberLabel.setText("No. Tiket: " + ticketId);
        } catch (WriterException e) {
            showAlert(Alert.AlertType.ERROR, "QR Code Error", "Gagal membuat QR Code: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        Ticket newTicket = new Ticket(ticketId, name, email, ticketQuantity, new java.util.Date());

        boolean success = saveTicketToDB(ticketId, name, email, ticketQuantity);
        if (!success) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal menyimpan tiket ke database.");
            return;
        }

        showAlert(Alert.AlertType.INFORMATION, "Pembelian Sukses!",
                "Tiket berhasil dibeli!\nNomor Tiket: " + ticketId + "\nQR Code Anda telah dibuat.");

        showFeedbackDialog(ticketId);

        nameField.clear();
        emailField.clear();
        ticketQuantity = 1;
        quantityField.setText(String.valueOf(ticketQuantity));
    }

    private boolean isValidEmail(String email) {
        int atIdx = email.indexOf('@');
        int dotIdx = email.lastIndexOf('.');
        return atIdx > 0 && dotIdx > atIdx + 1 && dotIdx < email.length() - 1;
    }

    private boolean saveTicketToDB(String ticketId, String name, String email, int quantity) {
        String sql = "INSERT INTO Visitor_Ticket (ticketID, name, email, ticketQuantity) VALUES (?, ?, ?, ?)";
        try (Connection conn = new DatabaseConnection().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, ticketId);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setInt(4, quantity);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showFeedbackDialog(String visitorID) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/exhibitly/FeedbackDialog.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Form Feedback");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(nameField.getScene().getWindow());
            dialogStage.setScene(new Scene(page));

            TextArea feedbackTextArea = (TextArea) page.lookup("#feedbackTextArea");
            Button submitFeedbackButton = (Button) page.lookup("#submitFeedbackButton");

            submitFeedbackButton.setOnAction(e -> {
                String comment = feedbackTextArea.getText();
                if (comment.isBlank()) {
                    showAlert(Alert.AlertType.WARNING, "Input Kosong", "Mohon isi masukan Anda.");
                    return;
                }
                saveFeedbackToDB(visitorID, 1, comment);
                dialogStage.close();
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Terima kasih atas masukan Anda!");
            });

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveFeedbackToDB(String visitorID, int exhibitID, String comment) {
        String sql = "INSERT INTO Feedback (visitorID, exhibitID, comment) VALUES (?, ?, ?)";
        try (Connection conn = new DatabaseConnection().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, visitorID);
            pstmt.setInt(2, exhibitID);
            pstmt.setString(3, comment);
            pstmt.executeUpdate();

            System.out.println("Feedback berhasil disimpan ke database.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage generateQRCodeImage(String text, int width, int height) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return bufferedImage;
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
    private void onTicketsButtonClick(ActionEvent event) {

    }

    @FXML
    private void onLogoButtonClick(ActionEvent event) {
        navigateToPage(event, "/org/example/exhibitly/LandingPage.fxml");
    }

    @FXML
    private void onLoginLogoutButtonClick(ActionEvent event) {
        if (session != null && session.isLoggedIn()) {
            handleLogout(event);
        } else {
            navigateToPage(event, "/org/example/exhibitly/login.fxml");
        }
    }

    @FXML
    private void onMaintenanceButtonClick(ActionEvent event) {
        navigateToPage(event, "/org/example/exhibitly/MaintenancePage.fxml");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}