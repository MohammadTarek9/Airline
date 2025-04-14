import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FlightDetailsController {

    @FXML
    private Button AddFlightBtn;

    @FXML
    private Button CancelFlightBtn;

    @FXML
    private Button DelayFlightBtn;

    @FXML
    private Button ViewBookingsBtn;

    @FXML
    private Button ViewFlightDetailsBtn;

    @FXML
    private TextField flightNumberField;

    @FXML
    private TableView<Booking> flights;

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
    void HandleViewBookings(ActionEvent event) {

        Admin admin = Session.getAdmin();
        String flightNumber = flightNumberField.getText();
        if (flightNumber.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a flight number.");
            return;
        }
        ArrayList<Booking> bookings = BookingsModel.getAllBookingsForFlightNumber(flightNumber);
        
        if (bookings.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Bookings", "No bookings found for flight number: " + flightNumber);
        } else {
            drawTable(bookings);
        }
        

    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void drawTable(ArrayList<Booking> bookings) {
        flights.getColumns().clear();

        ObservableList<Booking> bookingData = FXCollections.observableArrayList(bookings);
        TableColumn<Booking, String> bookingIdColumn = new TableColumn<>("Booking ID");
        bookingIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getBookingID())));

        TableColumn<Booking, String> passengerNameColumn = new TableColumn<>("Passenger Email");
        passengerNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPassenger().getEmail()));

        TableColumn<Booking, String> flightNumberColumn = new TableColumn<>("Flight Number");
        flightNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFlight().getFlightNumber()));

        TableColumn<Booking, String> seatNumberColumn = new TableColumn<>("Seat id");
        seatNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSeat().getSeat_id()));

        TableColumn<Booking, String> bookingDateColumn = new TableColumn<>("Confirmed");
        bookingDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIsConfirmed() ? "Yes" : "No"));

        flights.getColumns().addAll(bookingIdColumn, passengerNameColumn, flightNumberColumn, seatNumberColumn, bookingDateColumn);

        flights.setItems(bookingData);
        flights.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

}
