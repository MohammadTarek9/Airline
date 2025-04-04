import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.ArrayList;

public class BookFlightsController {

    @FXML
    private Button AccountBtn;

    @FXML
    private Button BookBtn;

    @FXML
    private Button BookFlightsBtn;

    @FXML
    private Button CancelFlightBtn;

    @FXML
    private DatePicker DateField;

    @FXML
    private TextField DestinationField;

    @FXML
    private TextField SourceField;

    @FXML
    private Button UpdatesBtn;

    @FXML
    private TableView<?> flights;

    @FXML
    void GoToCancelFlight(ActionEvent event) {

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CancelFlight.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) CancelFlightBtn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("FlyOps - Cancel Flight");
            stage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void GoToManageAccount(ActionEvent event) {

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageAccount.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) AccountBtn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("FlyOps - HomePage");
            stage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void GoToUpdates(ActionEvent event) {

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Updates.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) UpdatesBtn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("FlyOps - Updates");
            stage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void HandleBooking(ActionEvent event) {

        String source = SourceField.getText();
        String destination = DestinationField.getText();
        String date = DateField.getValue().toString();

        if(source.isEmpty() || destination.isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in source and destination fields.");
            return;
        }
        
        ArrayList<Flight> availableFlights = Passenger.searchFlights(source, destination);
        if(availableFlights.isEmpty()){
            showAlert(Alert.AlertType.INFORMATION, "No Flights Found", "No flights available for the selected route.");
            return;
        }
        
        

    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
