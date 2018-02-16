package cs2410.assn8.controllers;

import cs2410.assn8.viewer.Main;
import cs2410.assn8.model.MineField;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

public class MainController {
    private Stage mainStage;
    private Timer timer = new Timer();
    private Integer time = 0;
    private Boolean firstGame = true;
    public Integer lastTime;


    @FXML
    public MenuBar menuBar;
    @FXML
    public HBox scoreBoard;
    @FXML
    public BorderPane borderPane;

    @FXML
    private ScoreBoardController scoreBoardController;

    @FXML
    private MenuBarController menuBarController;

    public Integer getTime(){
        return time;
    }

    public MenuBarController getMenuController() {
        return menuBarController;
    }

    public ScoreBoardController getScoreBoardController(){
        return scoreBoardController;
    }

    private void setMineField(Node node){
        borderPane.setBottom(node);
    }


    public void newGame(Size size, Difficulty difficulty){
        scoreBoardController.setController(this);
        menuBarController.setController(this);
        scoreBoardController.setMain(mainStage);
        MineField mineField = new MineField();
        mineField.setMainController(this);
        setMineField(mineField.MineField1(size, difficulty));
        menuBarController.initGame();
        if(firstGame){
            menuBarController.setDefaultSize();
        }
        else {

        }
        firstGame = false;

    }


    public void mainStage(Stage mainPane) {
        mainStage = mainPane;
        mainStage.setOnCloseRequest(event -> timer.cancel());
    }
    public void timerStop(){
        if(timer != null) {
            Integer tmptime = time;
            timer.cancel();
            timer = new Timer();
            lastTime = time;
            time = 0;
        }
    }
    public void sTimer() {
        if(time != 0){
            timerStop();
        }
        if(timer !=  null) {
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    scoreBoardController.setTime(time.toString());
                    time++;
                }
            }, 0, 1000);
        }
    }

    public Integer getLastTime() {
        return lastTime;
    }
}
