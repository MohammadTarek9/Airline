import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.time.LocalDateTime;

public class AddFlightController {

    @FXML
    private Button AddBtn;

    @FXML
    private Button AddFlightBtn;

    @FXML
    private TextField ArrivalTimeField;

    @FXML
    private TextField BaseFareField;

    @FXML
    private Button CancelFlightBtn;

    @FXML
    private PasswordField CapacityField;

    @FXML
    private Button DelayFlightBtn;

    @FXML
    private TextField DepartureTimeField;

    @FXML
    private TextField DestinationField;

    @FXML
    private TextField FlightNumberField;

    @FXML
    private PasswordField SourceField;

    @FXML
    private Button ViewFlightDetailsBtn;

    @FXML
    private Button clearBtn;

    
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
    void GoToDelay(ActionEvent event) {

            
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("DelayFlight.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) DelayFlightBtn.getScene().getWindow();
                DelayFlightBtn.getScene().setRoot(root);
                stage.setTitle("FlyOps - Delay Flight");
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
    void clearData(ActionEvent event) {

        FlightNumberField.clear();
        SourceField.clear();
        DestinationField.clear();
        DepartureTimeField.clear();
        ArrivalTimeField.clear();
        CapacityField.clear();
        BaseFareField.clear();
    }

    @FXML
    void handleAddFlight(ActionEvent event) {

        String flightNumber = FlightNumberField.getText();
        String source = SourceField.getText();
        String destination = DestinationField.getText();
        String departureTime = DepartureTimeField.getText();
        String arrivalTime = ArrivalTimeField.getText();
        int capacity = 0;
        try{
        capacity = Integer.parseInt(CapacityField.getText());
        }catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number for capacity.");
            return;
        }
        double baseFare = 0;
        try{
        baseFare = Double.parseDouble(BaseFareField.getText());
        }
        catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number for base fare.");
            return;
        }

        Admin admin = Session.getAdmin();
        Flight flight = new Flight(flightNumber, capacity, 0, source, destination, departureTime, arrivalTime, baseFare);
        String result = admin.addFlight(flight);
        if (result.equals("success")) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Flight added successfully!");
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
