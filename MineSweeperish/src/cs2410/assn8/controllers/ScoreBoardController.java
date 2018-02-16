package cs2410.assn8.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Add a description of the class here
 *
 * @author Brady Bodily
 * @version xxx
 */
public class ScoreBoardController {
    private MainController mainController;
    private Stage mainStage;
    @FXML
    public HBox scoreBoard;
    @FXML
    public Text bombsLeft;
    @FXML
    public Button startBtn;
    @FXML
    public Text time;

    protected void setController(MainController mainController){this.mainController = mainController;}

    void setMain(Stage stage){ this.mainStage = stage;}

    void setTime(String string){time.setText(string);}


    @FXML
    protected void newGameAction() {
        mainController.timerStop();
        mainController.newGame(mainController.getMenuController().getSize(),mainController.getMenuController().getDificulty());
        mainStage.sizeToScene();
        mainStage.centerOnScreen();

    }

}
