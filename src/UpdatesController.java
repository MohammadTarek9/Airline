import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.util.ArrayList;

public class UpdatesController implements Initializable {

    @FXML
    private Label flightCancellationLabel;

    @FXML
    private Label flightDelaysLabel;

    @FXML
    private Button BookFlightBtn;

    @FXML
    private Button CancelFlightBtn;

    @FXML
    private Button ManageAccountBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Passenger currentPassenger = Session.getPassenger();
        ArrayList<Booking> bookings = currentPassenger.getBookings();
        if(bookings.isEmpty()) {
            flightCancellationLabel.setText("No flight cancellations at the moment.");
            flightDelaysLabel.setText("No flight delays at the moment.");
        } 
        else{
            for(Booking booking : bookings) {
                Flight flight = booking.getFlight();
                boolean isCancelled = flight.isCancelled();
                if (isCancelled) {
                    flightCancellationLabel.setText("Flight " + flight.getFlightNumber() + " has been cancelled.");
                } else {
                    flightCancellationLabel.setText("No flight cancellations at the moment.");
                }

                String delayReason = flight.handleDelays();
                if (delayReason != "nothing") {
                    flightDelaysLabel.setText("Flight " + flight.getFlightNumber() + " is delayed due to: " + delayReason);
                } else {
                    flightDelaysLabel.setText("No flight delays at the moment.");
                }
            }
        }
        
    }

    @FXML
    void GoToBookFlight(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BookFlights.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) BookFlightBtn.getScene().getWindow();
            BookFlightBtn.getScene().setRoot(root);
            stage.setTitle("FlyOps - Book Flight");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void GoToCancelFlight(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CancelFlight.fxml"));
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
    void GoToManageAccount(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageAccount.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ManageAccountBtn.getScene().getWindow();
            ManageAccountBtn.getScene().setRoot(root);
            stage.setTitle("FlyOps - Manage Account");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    

}