import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UpdatesController implements Initializable {

    @FXML
    private Button accountBtn;

    @FXML
    private Button bookBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private Label cancelationID;

    @FXML
    private Label delayID;

    @FXML
    private Button viewBoardingBtn;

    @FXML
    private TextField flightIDField;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Passenger currentPassenger = Session.getPassenger();
        ArrayList<Booking> bookings = new ArrayList<>(currentPassenger.getBookings());
        if(bookings.isEmpty()) {
            cancelationID.setText("No flight cancellations at the moment.");
            delayID.setText("No flight delays at the moment.");
        } 
        else{
            boolean cancellationFound = false;
            boolean delayFound = false;
            StringBuilder delayMessage = new StringBuilder();
            StringBuilder cancelationMessage = new StringBuilder();
    
            for (Booking booking : bookings) {
                Flight flight = booking.getFlight();
                boolean isCancelled = flight.isCancelled();
    
                if (isCancelled) {
                    currentPassenger.getBookings().remove(booking);
                    cancelationMessage.append("Flight ").append(flight.getFlightNumber()).append(" has been cancelled\n");
                    cancellationFound = true;
                }
    
                String delayReason = flight.handleDelays();
                if (!"nothing".equals(delayReason)) {
                    delayMessage.append("Flight ").append(flight.getFlightNumber()).append(" delayed due to ").append(delayReason).append("\n");
                    delayFound = true;
                }
            }

            if (cancellationFound) {
                cancelationID.setText("Flight cancellations:\n" + cancelationMessage.toString());
            } else {
                cancelationID.setText("No flight cancellations at the moment.");
            }

            if(delayFound) {
                delayID.setText("Flight delays:\n" + delayMessage.toString());
            }
            else{
                delayID.setText("No flight delays at the moment.");
            }
        }
    }

    @FXML
    void goToBoardingPass(ActionEvent event) {
        String flightID = flightIDField.getText();
        if (flightID.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a flight ID.");
            return;
        }
        Flight flight = FlightsModel.getFlightDetails(flightID);
        Passenger currentPassenger = Session.getPassenger();
        ArrayList<Booking> bookings = currentPassenger.getBookings();
        boolean flightFound = false;
        for (Booking booking : bookings) {
            if (booking.getFlight().getFlightNumber().equals(flightID)) {
                flightFound = true;
                break;
            }
        }
        if (!flightFound) {
            showAlert(Alert.AlertType.ERROR, "Error", "You do not have a booking for this flight.");
            return;
        }

        if (flight == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Flight not found.");
            System.out.println("Flight not found.");
            return;
        }
        Session.setFlight(flight);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BoardingPass.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Boarding Pass");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void goToBookFlight(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BookFlights.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) bookBtn.getScene().getWindow();
            bookBtn.getScene().setRoot(root);
            stage.setTitle("FlyOps - Book Flight");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void goToCancelFlight(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CancelFlight.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            cancelBtn.getScene().setRoot(root);
            stage.setTitle("FlyOps - Cancel Flight");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void goToManageAccount(ActionEvent event) {
            
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageAccount.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) accountBtn.getScene().getWindow();
                accountBtn.getScene().setRoot(root);
                stage.setTitle("FlyOps - Manage Account");
                stage.show();
            } catch (Exception e) {
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

}
