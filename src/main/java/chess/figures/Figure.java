package chess.figures;

import chess.Board;
import chess.util.Move;
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
    public abstract void markAttacks();

    public abstract List<Move> getAvailableMoves();

    public abstract String getShortName();

    public void nullAttack() {
        isAttByWhite = false;
        isAttByBlack = false;
    }

    public boolean isAttByOpponent() {
        if (this instanceof Field) {
            throw new IllegalArgumentException("Fields don't have color marks. " +
                    "Use this method only if you are 100% sure that you are referencing a figure.");
        }

        if (color.equalsIgnoreCase("white") && isAttByBlack)
            return true;

        return color.equalsIgnoreCase("black") && isAttByWhite;
    }

    /**
     * This is used to check empty fields (that are not marked with player color)
     * in the case of casteling.
     *
     * @param playerColor
     * @return
     */
    public boolean isAttByOpponent(String playerColor) {
        if (playerColor.equalsIgnoreCase("white") && isAttByBlack)
            return true;

        return playerColor.equalsIgnoreCase("black") && isAttByWhite;
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
