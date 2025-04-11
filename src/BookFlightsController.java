import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
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
    private TableView<Flight> flights;

    @FXML
    private Button SearchBtn;

    @FXML
    private TextField flightNumberBtn;

    @FXML
    void GoToCancelFlight(ActionEvent event) {

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CancelFlight.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) CancelFlightBtn.getScene().getWindow();
            CancelFlightBtn.getScene().setRoot(root);
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
            AccountBtn.getScene().setRoot(root);
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
            UpdatesBtn.getScene().setRoot(root);
            stage.setTitle("FlyOps - Updates");
            stage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void HandleBooking(ActionEvent event) {
        String flightNumber = flightNumberBtn.getText();
        if(flightNumber.isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in flight number field.");
            return;
        }
        Flight selectedFlight = FlightsModel.getFlightDetails(flightNumber);

        if (selectedFlight == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No matching flight found.");
            return;
        }

        Session.setFlight(selectedFlight);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BookingDetails.fxml"));
            Parent root = loader.load();

            BookingDetailsController controller = loader.getController();
            controller.initialize(selectedFlight.getFlightNumber());

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setTitle("Booking details");
            ((Button) event.getSource()).getScene().setRoot(root);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
         
    }

    @FXML
    void HandleSearch(ActionEvent event) {
        String source = SourceField.getText();
        String destination = DestinationField.getText();
        String date = null;
        if(DateField.getValue()!=null){
            date = DateField.getValue().toString();
        }

        if(source.isEmpty() || destination.isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in source and destination fields.");
            return;
        }
        Passenger passenger = Session.getPassenger();
        ArrayList<Flight> availableFlights = passenger.searchFlights(source, destination);
        if(date != null){
            availableFlights = passenger.filterFlights(availableFlights,date);
        }
        if(availableFlights.isEmpty()){
            showAlert(Alert.AlertType.INFORMATION, "No Flights Found", "No flights available for the selected route.");
            return;
        }

        drawTable(availableFlights);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }

    @FXML
    private void initialize() {
        drawTable(Flight.getAllAvailableFlights());
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


        flights.getColumns().addAll(flightColumn, departureTimeColumn, arrivalTimeColumn, fareColumn, capacityColumn, bookedSeatsColumn);


        flights.setItems(flightData);
        flights.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
}
