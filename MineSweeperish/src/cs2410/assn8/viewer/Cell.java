package cs2410.assn8.viewer;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;


/**
 * Add a description of the class here
 *
 * @author Brady Bodily
 * @version xxx
 */
public class Cell extends Button{
    public boolean isBomb;
    public int cellX;
    public int cellY;
    public int flagged = 0;
    protected boolean isGameEnd;
    public SimpleBooleanProperty gameEnd = new SimpleBooleanProperty(isGameEnd, "GameStatusPropery", false);

    public Cell(int height, int width, boolean status, int y, int x){
        cellY = y;
        cellX = x;
        isBomb = status;
        this.setMinSize(width,height);
        this.setMaxSize(width,height);

        this.setStyle("-fx-color:C4C0C0; -fx-background-radius: 0 0 0 0; -fx-font-size: 8");

    }


}
