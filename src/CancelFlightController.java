import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.ArrayList;

import java.net.URL;
import java.util.ResourceBundle;

public class CancelFlightController implements Initializable {

    @FXML
    private Button BookFlightBtn;

    @FXML
    private Button CancelFlight;

    @FXML
    private Button CancelFlightBtn;

    @FXML
    private TextField BookingIDField;

    @FXML
    private Button ManageAccBtn;

    @FXML
    private Button UpdatesBtn;

    @FXML
    private TableView flights;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        flights.getColumns().clear();

        TableColumn<Booking, String> bookingIDCol = new TableColumn<>("Booking ID");
        bookingIDCol.setCellValueFactory(new PropertyValueFactory<>("bookingID"));

        TableColumn<Booking, String> flightNumberCol = new TableColumn<>("Flight Number");
        flightNumberCol.setCellValueFactory(data -> {
            Flight flight = data.getValue().getFlight();
            return new SimpleStringProperty(flight != null ? flight.getFlightNumber() : "N/A");
        });

        TableColumn<Booking, String> seatIDCol = new TableColumn<>("Seat ID");
        seatIDCol.setCellValueFactory(data -> {
            Seat seat = data.getValue().getSeat();
            return new SimpleStringProperty(seat != null ? seat.getSeat_id() : "N/A");
        });
        

        flights.getColumns().addAll(bookingIDCol, flightNumberCol, seatIDCol);
        flights.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Passenger passenger = Session.getPassenger();
        //ArrayList<Booking> bookings = passenger.fetchBookings();
        ArrayList<Booking> bookings = passenger.getBookings();
        ArrayList<Flight> allFlights = new ArrayList<>();
        for (Booking booking : bookings) {
            allFlights.add(booking.getFlight());
        }
        ObservableList<Booking> flightData = FXCollections.observableArrayList(bookings);
        flights.setItems(flightData);
        flights.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


    }

    @FXML
    void GoToAccount(ActionEvent event) {

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageAccount.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ManageAccBtn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("FlyOps - Manage Account");
            stage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void GoToBooking(ActionEvent event) {

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BookFlights.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) BookFlightBtn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("FlyOps - Book Flight");
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
    void HandleCancellation(ActionEvent event) {

        String bookingID = BookingIDField.getText();
        if (bookingID.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Flight number cannot be empty!");
            return;
        }

        Passenger passenger = Session.getPassenger();
        ArrayList<Booking> passengerBookings = passenger.getBookings();
        if(passengerBookings == null || passengerBookings.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "No bookings found for this passenger!");
            return;
        }
        String result = passenger.cancelBooking(passengerBookings, bookingID);
        if(result.equals("success")){
            showAlert(Alert.AlertType.INFORMATION, "Success", "Booking cancelled successfully!");
            BookingIDField.clear();
            passenger.refreshBookings(); 
            refreshTable();
        }
        else{
            showAlert(Alert.AlertType.ERROR, "Error", result);
        }
        
    }

    private void refreshTable() {
        Passenger passenger = Session.getPassenger();
        ArrayList<Booking> updatedBookings = passenger.getBookings();
        ObservableList<Booking> bookingData = FXCollections.observableArrayList(updatedBookings);
        flights.setItems(bookingData);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
