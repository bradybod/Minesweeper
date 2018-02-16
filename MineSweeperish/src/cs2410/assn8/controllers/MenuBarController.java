package cs2410.assn8.controllers;


import cs2410.assn8.model.DataEntry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * Add a description of the class here
 *
 * @author Brady Bodily
 * @version xxx
 */
public class MenuBarController {
    @FXML
    public MenuBar menuBar;
    @FXML
    public RadioMenuItem smallSize;
    @FXML
    public RadioMenuItem mediumSize;
    @FXML
    public RadioMenuItem largeSize;
    @FXML
    public RadioMenuItem dificultyEasy;
    @FXML
    public RadioMenuItem dificultyMedium;
    @FXML
    public RadioMenuItem dificultyHard;
    @FXML
    public MenuItem viewScoreBoard;
    public RadioMenuItem sound;

    private ToggleGroup size = new ToggleGroup();
    private ToggleGroup difficulty = new ToggleGroup();
    private final String fileName = "data/scores.txt";
    private String scoresString = "";
    private ArrayList<DataEntry> arrayList = new ArrayList<>();


    private MainController mainController;

    protected void setController(MainController mainController){this.mainController = mainController;}

    public void setDefaultSize(){
        size.selectToggle(smallSize);
        difficulty.selectToggle(dificultyEasy);
    }
    public void initGame(){
        size();
        difficulty();
    }


    private void size(){
        smallSize.setToggleGroup(size);
        smallSize.setUserData(cs2410.assn8.controllers.Size.SMALL);
        mediumSize.setToggleGroup(size);
        mediumSize.setUserData(cs2410.assn8.controllers.Size.MEDIUM);
        largeSize.setToggleGroup(size);
        largeSize.setUserData(cs2410.assn8.controllers.Size.LARGE);
    }
    private void difficulty(){

        dificultyEasy.setToggleGroup(difficulty);
        dificultyEasy.setUserData(Difficulty.EASY);
        dificultyMedium.setToggleGroup(difficulty);
        dificultyMedium.setUserData(Difficulty.MEDIUM);
        dificultyHard.setToggleGroup(difficulty);
        dificultyHard.setUserData(Difficulty.HARD);
    }


    public Size getSize() {
        return (Size) size.getSelectedToggle().getUserData();
    }

    public Difficulty getDificulty() {
        return (Difficulty) difficulty.getSelectedToggle().getUserData();
    }

    public MainController getMainController() {
        return mainController;
    }

    @FXML
    public boolean soundHeard() {
        return sound.isSelected();
    }


    public void scoreBoardPane(ActionEvent actionEvent) {
        mainController.getScoreBoardController().bombsLeft.setText("");
        ArrayList<DataEntry> file = new ArrayList(fileRead());
        for(int i = 0; i < file.size(); i++) {
            scoresString += file.get(i).name1;
            scoresString += " ";
            scoresString += file.get(i).score1.toString();
            scoresString +="\n" ;
        }
        DialogPane label = new DialogPane();
        label.setContentText(scoresString);
        scoresString = "";
        label.setMinHeight(mainController.borderPane.getHeight()-75);
        mainController.borderPane.setBottom(label);
    }

    private ArrayList<DataEntry> fileRead() {
        ArrayList<DataEntry> tmp = new ArrayList<>();
        Scanner fileInput = null;
        try {
            fileInput = new Scanner(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assert fileInput != null;
        while (fileInput.hasNext()) {
            String name = fileInput.next();
            Integer score = Integer.parseInt(fileInput.next());
            DataEntry tmp1 = new DataEntry(name, score);
            tmp.add(tmp1);
        }
        fileInput.close();
        return tmp;
    }

    public void addRecord(String name, Integer score){
        arrayList.add(new DataEntry(name, score));
        fileWrite();
    }

    public void fileWrite() {
        PrintWriter fileOutput = null;
        try {
            fileOutput = new PrintWriter(new FileOutputStream(fileName, true)); //try changing to false
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (DataEntry anArrayList : arrayList) {
            fileOutput.println(anArrayList.name1 + anArrayList.score1.toString());
        }
        fileOutput.close();
    }
}
