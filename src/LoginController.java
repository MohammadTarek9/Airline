import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
    private TextField passwdField;

    @FXML
    void GoToSignUp(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) SignUpBtn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
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
        if(result.equals("Email and password cannot be empty!")){
            showAlert(Alert.AlertType.ERROR, "Error", "Email and password cannot be empty!");
        }
        else if(result.equals("You do not have an account!")){
            showAlert(Alert.AlertType.ERROR, "Error", "You do not have an account!");
        }
        else if(result.equals("Incorrect password!")){
            showAlert(Alert.AlertType.ERROR, "Error", "Incorrect password!");
        }
        else if(result.equals("success")){
            Passenger passenger = UserModel.getPassengerDetails(email);
            Session.setPassenger(passenger);
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageAccount.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) LoginBtn.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
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
