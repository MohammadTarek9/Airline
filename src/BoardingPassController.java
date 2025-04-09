import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class BoardingPassController implements Initializable {

    @FXML
    private Label ArrivalLabel;

    @FXML
    private Label DestinationLabel;

    @FXML
    private Label DestinationLabel1;

    @FXML
    private Label DestinationLabel11;

    @FXML
    private Label DestinationLabel12;

    @FXML
    private Label DestinationLabel121;

    @FXML
    private Label DestinationLabel1211;

    @FXML
    private Label DestinationLabel13;

    @FXML
    private Label DestinationLabel131;

    @FXML
    private Label arrivalLabel;

    @FXML
    private AnchorPane boardingPane;

    @FXML
    private Label departureLabel;

    @FXML
    private Label flightNumberLabel;

    @FXML
    private Label flightNumberlabel1;

    @FXML
    private Label gatelabel;

    @FXML
    private Label passengerlabel;

    @FXML
    private Label passengerlabel1;

    @FXML
    private Label schedulelabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Passenger currentPassenger = Session.getPassenger();
        Flight selectedFlight = Session.getFlight();
        
        flightNumberLabel.setText(selectedFlight.getFlightNumber());
        flightNumberlabel1.setText(selectedFlight.getFlightNumber());
        passengerlabel.setText(currentPassenger.getFirstName() + " " + currentPassenger.getLastName());
        passengerlabel1.setText(currentPassenger.getFirstName() + " " + currentPassenger.getLastName());
        schedulelabel.setText(selectedFlight.getDepartureTime());
        departureLabel.setText(selectedFlight.getDepartureTime());
        ArrivalLabel.setText(selectedFlight.getSource());
        arrivalLabel.setText(selectedFlight.getArrivalTime());
        DestinationLabel.setText(selectedFlight.getDestination());

    }

}
