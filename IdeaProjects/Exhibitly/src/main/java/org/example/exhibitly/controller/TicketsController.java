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
import javafx.stage.Stage;
// import org.example.exhibitly.Main; // TIDAK DIGUNAKAN LAGI
// import org.example.exhibitly.model.User; // TIDAK DIGUNAKAN LAGI
import org.example.exhibitly.models.Ticket; // Tetap digunakan untuk objek tiket, tapi tidak ada dependency User

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.UUID;

public class TicketsController extends BaseController implements Initializable {

    @FXML private ImageView logoFooter;
    @FXML private Button logoButton;
    @FXML private ImageView qrCodeImageView;
    @FXML private Label ticketNumberLabel;

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField quantityField;

    private int ticketQuantity = 1;

    // --- Inisialisasi Controller ---
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

        // Inisialisasi logo (opsional, jika Anda punya gambar logo)
        // Platform.runLater(() -> {
        //     logoButton.setImage(new Image(Objects.requireNonNull(getClass().getResource("/images/your_logo.png")).toExternalForm()));
        //     logoFooter.setImage(new Image(Objects.requireNonNull(getClass().getResource("/images/your_logo_footer.png")).toExternalForm()));
        // });
    }

    // --- Fungsionalitas Tombol Kuantitas Tiket ---
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

    // --- Fungsionalitas Tombol Beli Tiket ---
    @FXML
    private void onBuyButtonClick(ActionEvent event) {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || ticketQuantity <= 0) {
            showAlert(Alert.AlertType.WARNING, "Input Tidak Lengkap", "Nama, Email, dan Jumlah Tiket harus diisi dan valid.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Email Tidak Valid", "Format email tidak valid. Email harus mengandung '@', '.', dan domain yang benar (misal: user@domain.com).");
            return;
        }

        // --- Logika untuk Generate QR Code dan ID Tiket ---
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


        showAlert(Alert.AlertType.INFORMATION, "Pembelian Sukses!",
                "Tiket berhasil dibeli!\nNomor Tiket: " + ticketId + "\nQR Code Anda telah dibuat.");

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

    // --- Metode Helper untuk Generate QR Code (Menggunakan ZXing) ---
    private BufferedImage generateQRCodeImage(String text, int width, int height) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF); // Black on white
            }
        }
        return bufferedImage;
    }

    // --- Metode Navigasi (Sama seperti Controller lainnya) ---

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
        // loadScene("/org/example/exhibitly/tickets-view.fxml", event);
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

    // --- Metode Helper untuk Menampilkan Alert ---
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}