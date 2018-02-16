package cs2410.assn8.model;

import cs2410.assn8.controllers.Difficulty;
import cs2410.assn8.controllers.MainController;
import cs2410.assn8.controllers.Size;
import cs2410.assn8.viewer.Cell;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

/**
 * Add a description of the class here
 *
 * @author Brady Bodily
 * @version xxx
 */

public class MineField extends GridPane {
    private  boolean isGameEnd;
    private SimpleBooleanProperty gameEnd = new SimpleBooleanProperty(isGameEnd, "GameStatusPropery", false);
    private Integer bombCount, rowNum, colNum, cellH, cellW;
    private int disabledCount = 1;
    private GridPane gridPane = new GridPane();
    private ArrayList<ArrayList<Cell>> cells = new ArrayList<>();
    private ArrayList<Cell> tmp = new ArrayList<>();
    private String mineFilePath = "file:data/mine.gif";
    private String flagFilePath = "file:data/flag.png";
    private String questionMarkFilePath = "file:data/questionmark.png";
    private String win = "file:data/win.jpg";
    private MainController mainController;
    private Integer bombsFound = 0;
    private boolean firstClick = true;
    private MediaPlayer gameOver;
    private MediaPlayer gameWon;
    private MediaPlayer click;
    private MediaPlayer flagSound;
    private MediaPlayer bombClicked;


    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public GridPane MineField1(Size size, Difficulty difficulty ) {
        setSettings(size, difficulty);
        Integer count = 0;
        gameOver = new MediaPlayer(new Media(new File("data/sounds/lose.wav").toURI().toString()));
        gameOver.setOnEndOfMedia(()-> gameOver.stop());

        gameWon = new MediaPlayer(new Media(new File("data/sounds/win.mp3").toURI().toString()));
        gameWon.setOnEndOfMedia(()-> gameWon.stop());

        click = new MediaPlayer(new Media(new File("data/sounds/click.wav").toURI().toString()));
        click.setOnEndOfMedia(()-> click.stop());

        flagSound = new MediaPlayer(new Media(new File("data/sounds/flag.mp3").toURI().toString()));
        flagSound.setOnEndOfMedia(()-> flagSound.stop());

        bombClicked = new MediaPlayer(new Media(new File("data/sounds/bomb.wav").toURI().toString()));
        bombClicked.setOnEndOfMedia(()-> bombClicked.stop());

            for (int j = 0; j < colNum*rowNum; j++) {
                if (count < bombCount) {
                    Cell cell = new Cell(cellH, cellW, true, 0, 0);
                    cell.gameEnd.bindBidirectional(gameEnd);
                    addListner(cell);
                    tmp.add(cell);
                    count++;

                } else {
                    Cell cell = new Cell(cellH, cellW, false, 0, 0);
                    cell.gameEnd.bindBidirectional(gameEnd);
                    addListner(cell);
                    tmp.add(cell);
                    count++;
                }
            }
        Collections.shuffle(tmp);
        bombsFound = bombCount;

        int position = 0;
        for(int i = 0; i < rowNum; i++){
            cells.add(new ArrayList<Cell>());
            for(int j = 0; j < colNum; j++) {
                tmp.get(position).cellY = i;
                tmp.get(position).cellX = j;
                cells.get(i).add(tmp.get(position));
                position++;
            }
        }
        mainController.getScoreBoardController().bombsLeft.setText(bombCount.toString());
        return createGridPane();
    }

    private void addListner(Cell cell) {
        cell.setOnMouseClicked(event -> {
            if(firstClick){
                mainController.sTimer();
                firstClick = false;
            }
            if(event.getButton() == MouseButton.PRIMARY && cell.flagged == 0) {
                if(mainController.getMenuController().soundHeard()) {
                    click.play();
                }
                if (cell.isBomb ) {
                    ImageView mine = new ImageView(mineFilePath);
                    mine.setFitWidth(cellW-35);
                    mine.setFitHeight(cellH-35);
                    mine.setPreserveRatio(true);
                    cell.setGraphic(mine);
                    if(mainController.getMenuController().soundHeard()) {
                        bombClicked.play();
                    }
                    bombClicked.setOnEndOfMedia(()->{
                        gameOver.setVolume(1);
                        gameOver.play();
                    });
                    Alert alert = new Alert( Alert.AlertType.INFORMATION);
                    alert.setTitle("You Lose");
                    alert.setHeaderText("You lost the game, play again");
                    alert.setGraphic(new ImageView(mineFilePath));
                    alert.show();

                    isGameEnd = true;
                    mainController.timerStop();
                    cell.gameEnd.setValue(true);
                    gridPane.setDisable(true);
                } else {
                    recursion(cell.cellY, cell.cellX);
                }
            }else if(event.getButton() == MouseButton.SECONDARY && cell.flagged == 0){
                if(mainController.getMenuController().soundHeard()) {
                    flagSound.play();
                }
                //flag
                ImageView flag = new ImageView(flagFilePath);
                bombsFound--;
                mainController.getScoreBoardController().bombsLeft.setText(bombsFound.toString());
                flag.setFitWidth(cellW-35);
                flag.setFitHeight(cellH-35);
                flag.setPreserveRatio(true);
                cell.setGraphic(flag);
                cell.flagged = 1;

            }else if(event.getButton() == MouseButton.SECONDARY && cell.flagged == 1){
                if(mainController.getMenuController().soundHeard()) {
                    flagSound.play();
                }
                //question
                ImageView question = new ImageView(questionMarkFilePath);
                question.setFitWidth(cellW-35);
                question.setFitHeight(cellH-35);
                question.setPreserveRatio(true);
                cell.setGraphic(question);
                bombsFound++;
                mainController.getScoreBoardController().bombsLeft.setText(bombsFound.toString());
                cell.flagged = 2;

            }else if(event.getButton() == MouseButton.SECONDARY && cell.flagged == 2){
                if(mainController.getMenuController().soundHeard()) {
                    flagSound.play();
                }
                //setBack to normal
                cell.setGraphic(null);
                cell.flagged = 0;
            }
        });

        cell.gameEnd.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    gridPane.setDisable(true);
                    ImageView mines = new ImageView(mineFilePath);
                    mines.setFitHeight(cellH-35);
                    mines.setFitWidth(cellW-35);
                    mines.setPreserveRatio(true);

                    if(cell.isBomb){
                        cell.setDisable(true);
                        cell.setGraphic(mines);
                        if(cell.flagged == 1){
                            cell.setStyle("-fx-background-color: green");
                        }else {
                            cell.setStyle("-fx-background-color: red");
                        }
                    }else{
                        if(cell.flagged == 1){
                            cell.setStyle("-fx-background-color: yellow");
                        }
                    }
                }
            }
        });
    }

    private void recursion(int y, int x){
        Integer nearbyBombCount = 0;

            if (y >= rowNum || y < 0 || x >= colNum || x < 0 || cells.get(y).get(x).isBomb || cells.get(y).get(x).isDisabled()) {
                return;
            } else {
                if(disabledCount < (colNum*rowNum-bombCount)) {
                    cells.get(y).get(x).setDisable(true);
                    disabledCount++;
                } else{
                    gameWon.play();
                    mainController.timerStop();
                    cells.get(y).get(x).gameEnd.setValue(true);
                    TextInputDialog alert = new TextInputDialog("unk");
                    alert.setHeaderText("You just won the Game in " + mainController.getLastTime().toString() + " seconds!");
                    alert.setContentText("Enter your initials!");
                    alert.setGraphic(new ImageView(win));
                    Optional var = alert.showAndWait();
                    if(var.isPresent()) {
                        String tmp = var.get() + " ";
                        mainController.getMenuController().addRecord(tmp, mainController.lastTime);
                    }
                }
            }
            nearbyBombCount += checkBound(y - 1, x - 1);
            nearbyBombCount += checkBound(y - 1, x);
            nearbyBombCount += checkBound(y - 1, x + 1);
            nearbyBombCount += checkBound(y, x - 1);
            nearbyBombCount += checkBound(y, x + 1);
            nearbyBombCount += checkBound(y + 1, x - 1);
            nearbyBombCount += checkBound(y + 1, x);
            nearbyBombCount += checkBound(y + 1, x + 1);

            if (nearbyBombCount == 0) {
                recursion(y - 1, x - 1);
                recursion(y - 1, x);
                recursion(y - 1, x + 1);
                recursion(y, x - 1);
                recursion(y, x + 1);
                recursion(y + 1, x - 1);
                recursion(y + 1, x);
                recursion(y + 1, x + 1);
            } else {

                cells.get(y).get(x).setText(nearbyBombCount.toString());
            }
        }

    private int checkBound(int y, int x){
        if(y >= rowNum || y < 0 || x >= colNum || x < 0) {
            return 0;
        }else if (cells.get(y).get(x).isBomb && !cells.get(y).get(x).isDisabled()){
            return 1;
        }
        return 0;
    }

    private GridPane createGridPane() {
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                gridPane.add(cells.get(i).get(j), i, j);
            }
        }
        return gridPane;
    }

    private void setSettings(Size size, Difficulty difficulty){
        switch (size){
            case SMALL:
                rowNum = 10;
                colNum = 10;
                cellH = 50;
                cellW = 50;
                break;
            case MEDIUM:
                rowNum = 25;
                colNum = 25;
                cellH = 35;
                cellW = 35;
                break;
            case LARGE:
                rowNum = 50;
                colNum = 25;
                cellH = 22;
                cellW = 22;
                break;
        }
        switch (difficulty){
            case EASY:
                bombCount = (int)(0.1*(colNum*rowNum));
                break;
            case MEDIUM:
                bombCount = (int)(0.25*(colNum*rowNum));
                break;
            case HARD:
                bombCount = (int)(0.5*(colNum*rowNum));
                break;
            default:
                bombCount = (int)(0.1*(colNum*rowNum));
                break;
        }

    }

}
