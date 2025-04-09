
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import java.util.List;

public class BookingDetailsController {
    @FXML
    private GridPane seatGrid;

    private String selectedSeatId;

    @FXML
    private Label seatID; 
    @FXML private Label flightNum;
    @FXML private Label fareID;
    @FXML private Label capacityID;
    @FXML private Label bookedSeatsID;
    @FXML private Label destinationID;
    @FXML private Label departureTimeID;
    @FXML private Label arrivalTimeID;
    @FXML private Label sourceID;
    @FXML private Button goToPaymentBtn;

    public void initialize(String flightNumber) {
        SeatModel.connectToDatabase(); // Ensure DB is connected
    
        // Display seats
        List<Seat> seats = SeatModel.getAllSeats(flightNumber);
        displaySeats(seats);
    
        // Display flight info
        Flight flight = FlightsModel.getFlightDetails(flightNumber);
        if (flight != null) {
            flightNum.setText(flight.getFlightNumber());
            fareID.setText(String.valueOf(flight.getBaseFare()));
            capacityID.setText(String.valueOf(flight.getCapacity()));
            bookedSeatsID.setText(String.valueOf(flight.getBookedSeats()));
            destinationID.setText(flight.getDestination());
            sourceID.setText(flight.getSource());
            departureTimeID.setText(flight.getDepartureTime());
            arrivalTimeID.setText(flight.getArrivalTime());
        }
    }

    private void displaySeats(List<Seat> seats) {
        seatGrid.getChildren().clear();
        int row = 0, col = 0;

        for (Seat seat : seats) {
            Button seatButton = new Button(seat.getSeat_id()+" "+seat.getSeatType());
            seatButton.setMinSize(60, 60);

            if (!seat.isAvailable()) {
                seatButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                seatButton.setDisable(true);
            } else {
                seatButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                seatButton.setOnMouseClicked(this::handleSeatSelection);
            }

            seatGrid.add(seatButton, col, row);
            col++;
            if (col == 6) {
                col = 0;
                row++;
            }
        }
    }

    private void handleSeatSelection(MouseEvent event) {
        Button clickedButton = (Button) event.getSource();
        selectedSeatId = clickedButton.getText().split(" ")[0]; // Extract seat ID only
        seatID.setText(selectedSeatId); // Update label
    }

    public String getSelectedSeatId() {
        return selectedSeatId;
    }

    @FXML
void GoToManageAccount(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageAccount.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        ((Button) event.getSource()).getScene().setRoot(root);
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Updates.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        ((Button) event.getSource()).getScene().setRoot(root);
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
            ((Button) event.getSource()).getScene().setRoot(root);
            stage.setTitle("Cancel Flights");
            stage.setResizable(false);
            stage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

@FXML
void GoTopayment(ActionEvent event) {
    // Check if a seat is selected
    if (seatID.getText() == null || seatID.getText().trim().isEmpty()) {
        showAlert(Alert.AlertType.ERROR, "Error", "Please choose a seat.");
        return;
    }
        Passenger passenger = Session.getPassenger();
        String seat_id = seatID.getText();
        boolean res = passenger.BookFlight(seat_id);
        if(!res) {
        showAlert(Alert.AlertType.ERROR, "Error", "Booking failed. Please try again.");
        return;
        }
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Payment.fxml"));
        Parent root = loader.load();

        PaymentController controller = loader.getController();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setTitle("Payment");
        ((Button) event.getSource()).getScene().setRoot(root);
        stage.setResizable(false);
        stage.show();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

}
