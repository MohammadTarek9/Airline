import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
            Scene scene = new Scene(root);
        
            primaryStage.setTitle("FlyOps - Sign Up");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
       
    }

    public static void main(String[] args) {
        String JDBC_URL = "jdbc:mysql://localhost:3306/airvista";
        String USERNAME = "root";
        String PASSWORD = "root";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            if(connection != null){
                System.out.println("Connection established");
            }
            else{
                System.out.println("Connection failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        launch(args);
    }
}
