package chess.figures;

import chess.Board;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

import javax.swing.ImageIcon;

@Getter
@Setter
public abstract class Figure implements Serializable {

    protected boolean isAttByWhite;
    protected boolean isAttByBlack;
    protected ImageIcon icon;
    protected String color;
    protected int[] position = new int[2];
    protected boolean moved;
    protected Board board;

    //Main Move Checker
    public abstract boolean isTargetLocationValid(int x, int y);

    //Attacking functions
    public abstract void setAttacks();

    public void resetAttack() {
        isAttByWhite = false;
        isAttByBlack = false;
    }

    public boolean isAttByOpponent(String figureColor) {
        if (figureColor.equalsIgnoreCase("white") && isAttByBlack)
            return true;

        return figureColor.equalsIgnoreCase("black") && isAttByWhite;
    }

    public void setAttackedBy(String Color) {
        if (Color.equalsIgnoreCase("white"))
            isAttByWhite = true;
        else
            isAttByBlack = true;
    }

    public int getX() {
        return position[0];
    }

    public int getY() {
        return position[1];
    }

    public void setPosition(int x, int y) {
        position[0] = x;
        position[1] = y;
    }

}