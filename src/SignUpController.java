import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
    private TextField PasswdField;

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
            LoginController loginController = new LoginController();
            Parent root = loader.load();
            Stage stage = (Stage) LoginBtn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("FlyOps - Login");
            stage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void HandleSignUp(ActionEvent event) {
        try{
        String firstName = FNameField.getText();
        String lastName = LNameField.getText();
        String phone = PhoneNumField.getText();
        String email = EmailField.getText();
        String password = PasswdField.getText();
        int age = Integer.parseInt(AgeField.getText());

        Passenger passenger = new Passenger(firstName, lastName, phone, email, password, age);
        passenger.createAccount(email, password, firstName, lastName, phone, AgeField.getText());
        }

        catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
       

    }

}
