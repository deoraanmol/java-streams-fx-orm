import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloFX extends Application {

    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(
                getClass().getClassLoader().getResource("fx_samples/sample.fxml"));
        primaryStage.setTitle("Milestone 3: Visual Sensitivity Analysis");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
