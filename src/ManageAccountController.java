import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
public class ManageAccountController {
    @FXML
    private TextField AgeField;

    @FXML
    private Button BookFlightBtn;

    @FXML
    private Button CancelFlightBtn;

    @FXML
    private Button ManageAccountBtn;

    @FXML
    private TextField PhoneNumField;

    @FXML
    private Button UpdatesBtn;

    @FXML
    private Button clearBtn;

    @FXML
    private TextField confPasswdField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwdField;

    @FXML
    private Button saveBtn;

    @FXML
    void GoToBookFlight(ActionEvent event) {
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
    void GoToCancelFlight(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CancelFlight.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) CancelFlightBtn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("FlyOps - Cancel Flight");
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
    void clearData(ActionEvent event) {
        AgeField.clear();
        PhoneNumField.clear();
        emailField.clear();
        passwdField.clear();
        confPasswdField.clear();
    }
    
    @FXML
    void saveData(ActionEvent event) {
        String newEmail = emailField.getText();
        String newPassword = passwdField.getText();
        String confirmPassword = confPasswdField.getText();
        String newPhoneNumber = PhoneNumField.getText();
        String newAge = AgeField.getText();

        Passenger passenger = Session.getPassenger();

        String oldEmail = passenger.getEmail();

        String result = passenger.updateAccount(oldEmail, newEmail, newPassword, confirmPassword, newPhoneNumber, newAge);

        if(result.equals("success")){
            showAlert(Alert.AlertType.INFORMATION, "Success", "Account updated successfully!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", result);
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
