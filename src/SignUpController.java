

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUpController {

    @FXML
    private TextField AgeField;

    @FXML
    private TextField EmailField;

    @FXML
    private TextField FNameField;

    @FXML
    private TextField LNameField;

    @FXML
    private Button LoginBtn;

    @FXML
    private PasswordField PasswdField;

    @FXML
    private TextField PhoneNumField;

    @FXML
    private Button SignUpBtn;

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void GoToLogin(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) LoginBtn.getScene().getWindow();
            LoginBtn.getScene().setRoot(root);
            stage.setTitle("FlyOps - Login");
            stage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void HandleSignUp(ActionEvent event) {
        String firstName = FNameField.getText();
        String lastName = LNameField.getText();
        String phone = PhoneNumField.getText();
        String email = EmailField.getText();
        String password = PasswdField.getText();
        String age = AgeField.getText();
        String result = Passenger.createAccount(email, password, firstName, lastName, phone, age);
        if(result.equals("All fields are required!")){
            showAlert(Alert.AlertType.ERROR, "Error", "Email and password cannot be empty!");
        }
        else if(result.equals("Age must be a number!")){
            showAlert(Alert.AlertType.ERROR, "Error", "Age must be a number!");
        }
        else if(result.equals("Invalid email format!")){
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid email format!");
        }

        else if(result.equals("Email already exists!")){
            showAlert(Alert.AlertType.ERROR, "Error", "Email already exists!");
        }
        
        else if(result.equals("Invalid phone number format!")){
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid phone number format!");
        }
        else if(result.equals("Password must be at least 6 characters long!")){
            showAlert(Alert.AlertType.ERROR, "Error", "Password must be at least 6 characters long!");
        }
        else if(result.equals("success")){
            Passenger passenger = new Passenger(firstName, lastName, phone, email, password, Integer.parseInt(age));
            ArrayList<Booking> bookings = BookingsModel.getAllBookings(email);
            ArrayList<Seat> passengerSeats = new ArrayList<>();
            ArrayList<Flight> flights = new ArrayList<>();
            
            if(bookings != null){
            int j = 0;
            for(Booking booking : bookings){
                flights.add(BookingsModel.getBookingFlight(booking.getBookingID()));
                bookings.get(j).setFlight(flights.get(j));
                j++;
            }
            int i = 0;
            for(Booking booking : bookings){
                passengerSeats.add(BookingsModel.getBookingSeat(booking.getBookingID()));
                bookings.get(i).setSeat(passengerSeats.get(i));
                i++;
            }
        }
            passenger.setBookings(bookings);
            Session.setPassenger(passenger);
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageAccount.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) SignUpBtn.getScene().getWindow();
                SignUpBtn.getScene().setRoot(root);
                stage.setTitle("Homepage - Login");
                stage.show();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
       

    }

}
