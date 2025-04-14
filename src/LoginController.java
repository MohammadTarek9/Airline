

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

public class LoginController {

    @FXML
    private Button LoginBtn;

    @FXML
    private Button SignUpBtn;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwdField;

    @FXML
    void GoToSignUp(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) SignUpBtn.getScene().getWindow();
            SignUpBtn.getScene().setRoot(root);
            stage.setTitle("FlyOps - Sign Up");
            stage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void HandleLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwdField.getText();
        String result = Passenger.login(email, password);
        boolean isAdmin = false;
        if(result.equals("Email and password cannot be empty!")){
            showAlert(Alert.AlertType.ERROR, "Error", "Email and password cannot be empty!");
        }
        else if(result.equals("You do not have an account!")){
            if(Admin.login(email, password).equals("success")){
                isAdmin = true;
                Admin admin = AdminModel.getAdminDetails(email);
                Session.setAdmin(admin);
                try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("FlightDetails.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) LoginBtn.getScene().getWindow();
                    LoginBtn.getScene().setRoot(root);
                    stage.setTitle("FlyOps - Admin Page");
                    stage.show();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                showAlert(Alert.AlertType.ERROR, "Error", Admin.login(email, password));
            }
        }
        else if(result.equals("Incorrect password!")){
            showAlert(Alert.AlertType.ERROR, "Error", "Incorrect password!");
        }
        else if(result.equals("success")){
            Passenger passenger = UserModel.getPassengerDetails(email);
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
                Stage stage = (Stage) LoginBtn.getScene().getWindow();
                LoginBtn.getScene().setRoot(root);
                stage.setTitle("FlyOps - Home Page");
                stage.show();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

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
