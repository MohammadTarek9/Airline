import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class PaymentController {

@FXML
private TextField CCNumber;

@FXML
private TextField CCHolderName;

@FXML
private DatePicker ExDate;

@FXML
private PasswordField CCV;

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
// used for setting variables from previous scene
private String seatId;
private String flightNumber;

public void setSeatAndFlight(String seatId, String flightNumber) {
    this.seatId = seatId;
    this.flightNumber = flightNumber;
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

    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BoardingPass.fxml"));
        Parent root = loader.load();

        SeatModel.updateSeatAvailability(seatId, flightNumber, false);
        FlightsModel.incrementBookedSeats(flightNumber);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Boarding Pass");
        stage.setResizable(false);
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

@FXML
void GoToManageAccount(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageAccount.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Manage Account");
        stage.setResizable(false);
        stage.show();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

@FXML
void GoToUpdates(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard-Updates.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Updates");
        stage.setResizable(false);
        stage.show();
    } catch (Exception e) {
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
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
