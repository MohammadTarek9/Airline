
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
            Scene scene = new Scene(root);
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));
            primaryStage.setMaximized(true);
            primaryStage.setTitle("FlyOps - Sign Up");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
       
    }

    public static void main(String[] args) {
        UserModel.connectToDatabase();
        FlightsModel.connectToDatabase();
        BookingsModel.connectToDatabase();
        SeatModel.connectToDatabase();
        launch(args);
    }
}
