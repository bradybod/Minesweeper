package cs2410.assn8.viewer;

import cs2410.assn8.controllers.Difficulty;
import cs2410.assn8.controllers.MainController;
import cs2410.assn8.controllers.Size;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/cs2410/assn8/resources/Main.fxml"));
        Parent mainPane = loader.load();
        MainController controller = loader.getController();

        Alert taAlert = new Alert(Alert.AlertType.INFORMATION);
        taAlert.setTitle("Things I Have Implemented");
        taAlert.setHeaderText("Hello");
        taAlert.setGraphic(new ImageView("file:data/mine.gif"));
        taAlert.setContentText("Things I Have Implemented- \n\n" +
                "10pts - Size feature is fully implemented \n" +
                "10pts - Difficulty feature is fully implemented \n" +
                "15pts - Sound feature is fully implemented \n" +
                "5pts  - Game reporting feature is fully implemented but clunky\n\n" +
                "Thanks have a good break!");
        taAlert.getDialogPane().setMinWidth(450);
        Stage stage = (Stage) taAlert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.show();

        controller.mainStage(primaryStage);
        controller.newGame(Size.SMALL, Difficulty.EASY);


        primaryStage.setTitle("Mine Sweeperish");
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.resizableProperty().setValue(false);
        primaryStage.show();
        
    }


    public static void main(String[] args) {
        launch(args);
    }
}
