import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CancelFlightsAdminController implements Initializable {

    @FXML
    private Button AddFlightBtn;

    @FXML
    private Button CancelFlightBtn;

    @FXML
    private Button DelayFlightBtn;

    @FXML
    private TextField FlightNumField;

    @FXML
    private Button ViewFlightDetailsBtn;

    @FXML
    private Button cancelFlightBtn;

    @FXML
    private TableView<Flight> flights;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        drawTable(Flight.getAllAvailableFlights());
    }

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
    void HandleCancel(ActionEvent event) {

        String flightNumber = FlightNumField.getText().trim();
        if (flightNumber.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a flight number.");
            return;
        }

        Flight flight = FlightsModel.getFlightDetails(flightNumber);
        if (flight == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Flight not found.");
            return;
        }
        else{
            boolean res = FlightsModel.cancelFlight(flightNumber);
            if (res) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Flight cancelled successfully.");
                FlightNumField.clear();
                drawTable(Flight.getAllAvailableFlights());
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to cancel flight.");
            }
        }



    }

    private void drawTable(ArrayList<Flight> availableFlights) {
        flights.getColumns().clear();

        ObservableList<Flight> flightData = FXCollections.observableArrayList(availableFlights);
        TableColumn<Flight, String> flightColumn = new TableColumn<>("Flight Number");
        flightColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFlightNumber()));

        TableColumn<Flight, String> departureTimeColumn = new TableColumn<>("Departure Time");
        departureTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartureTime()));

        TableColumn<Flight, String> arrivalTimeColumn = new TableColumn<>("Arrival Time");
        arrivalTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArrivalTime()));

        TableColumn<Flight, String> fareColumn = new TableColumn<>("Fare");
        fareColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getBaseFare())));

        TableColumn<Flight, String> capacityColumn = new TableColumn<>("Capacity");
        capacityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCapacity())));

        TableColumn<Flight, String> bookedSeatsColumn = new TableColumn<>("Booked Seats");
        bookedSeatsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getBookedSeats())));

        
        TableColumn<Flight, String> sourceColumn = new TableColumn<>("Source");
        sourceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSource()));
        
        TableColumn<Flight, String> destinationColumn = new TableColumn<>("Destination");
        destinationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDestination()));


        flights.getColumns().addAll(flightColumn, departureTimeColumn, arrivalTimeColumn, fareColumn, capacityColumn, bookedSeatsColumn, sourceColumn, destinationColumn);


        flights.setItems(flightData);
        flights.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
