import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.time.LocalDateTime;

public class DelayFlightController {

    @FXML
    private Button AddFlightBtn;

    @FXML
    private Button CancelFlightBtn;

    @FXML
    private Button DelayFlightBtn;

    @FXML
    private TextArea DelayReasonField;

    @FXML
    private TextField NewArrivalField;

    @FXML
    private TextField NewDepartureField;

    @FXML
    private Button ViewFlightDetailsBtn;

    @FXML
    private TextField flightNumField;

    @FXML
    void GoToAddFlight(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddFlight.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) CancelFlightBtn.getScene().getWindow();
            CancelFlightBtn.getScene().setRoot(root);
            stage.setTitle("FlyOps - Add Flight");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void GoToCancel(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CancelFlightsAdmin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) CancelFlightBtn.getScene().getWindow();
            CancelFlightBtn.getScene().setRoot(root);
            stage.setTitle("FlyOps - Cancel Flight");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void GoToFlightDetails(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FlightDetails.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ViewFlightDetailsBtn.getScene().getWindow();
            ViewFlightDetailsBtn.getScene().setRoot(root);
            stage.setTitle("FlyOps - Flight Details");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void HandleDelay(ActionEvent event) {

        String flightNum = flightNumField.getText();
        String newArrival = NewArrivalField.getText();
        String newDeparture = NewDepartureField.getText();
        String delayReason = DelayReasonField.getText();

        if (flightNum.isEmpty() || newArrival.isEmpty() || newDeparture.isEmpty() || delayReason.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in all fields.");
            return;
        }

        Flight flight = FlightsModel.getFlightDetails(flightNum);
        if (flight == null) {
            showAlert(Alert.AlertType.ERROR, "Flight Not Found", "The flight number you entered does not exist.");
            return;
        }

        String result = flight.delayFlight(newDeparture, newArrival, delayReason);
        if (result.equals("success")) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Flight delayed successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", result);
        }

    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
