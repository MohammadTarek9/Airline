import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
public class PaymentController implements Initializable {

@FXML
private TextField CCNumber;

@FXML
private TextField CCHolderName;

@FXML
private DatePicker ExDate;

@FXML
private PasswordField CCV;

@FXML
private Label amount;

@Override
public void initialize(URL url, ResourceBundle resourceBundle) {
    TextFormatter<String> formatter = new TextFormatter<>(change -> {
        if (change.getControlNewText().matches("\\d{0,3}")) {
            return change;
        }
        return null;
    });
    CCV.setTextFormatter(formatter);
    Booking passengerBooking = Session.getPassenger().getBookings().get(Session.getPassenger().getBookings().size() - 1);
    System.out.println("Passenger BookingID: " + passengerBooking.getBookingID());
    Flight flight = passengerBooking.getFlight();
    System.out.println("Flight Number: " + flight.getFlightNumber());
    System.out.println("Flight Fare: " + flight.getBaseFare());
    Seat seat = passengerBooking.getSeat();
    amount.setText(String.valueOf(flight.calculateFare(seat.getSeatType())));

}

@FXML
private void clearPaymentFields() {
    CCNumber.clear();
    CCHolderName.clear();
    ExDate.setValue(null);
    CCV.clear();
}
private void showAlert(Alert.AlertType alertType, String title, String message) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}

@FXML
private void handlePayment(ActionEvent event) {

    if (CCNumber.getText().isEmpty() || 
        CCHolderName.getText().isEmpty() || 
        ExDate.getValue() == null || 
        CCV.getText().isEmpty()) {
        
        showAlert(Alert.AlertType.WARNING, "Missing Fields", "Please fill in all the payment details.");
        return;
    }
    String cardNumber = CCNumber.getText();
    int ccv = Integer.parseInt(CCV.getText());
    LocalDate date = ExDate.getValue();
    Booking currentBooking = Session.getPassenger().getBookings().get(Session.getPassenger().getBookings().size() - 1);
    boolean confirmed = currentBooking.confirmBooking(cardNumber, ccv, date);
    if (confirmed) {
        showAlert(Alert.AlertType.INFORMATION, "Success", "Payment successful! Booking confirmed.");
        clearPaymentFields();
    } else {
        showAlert(Alert.AlertType.ERROR, "Error", "Payment failed. Please try again.");
        return;
    }

    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BoardingPass.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Boarding Pass");
        stage.show();
        GoToCancelFlight(event);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    @FXML
    void GoToCancelFlight(ActionEvent event) {

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CancelFlight.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setTitle("Cancel Flights");
            ((Button) event.getSource()).getScene().setRoot(root);
            stage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
