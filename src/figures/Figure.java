package figures;

import java.awt.Image;
import java.io.Serializable;

import javax.swing.ImageIcon;

public abstract class Figure implements Serializable {

    protected boolean isAttByWhite;
    protected boolean isAttByBlack;
    protected ImageIcon Icon;
    protected String Color;
    protected int[] Position = new int[2];
    protected boolean Moved;

    //Main Move Checker
    public abstract boolean isMoveValid(int x, int y);

    //Attacking functions
    public abstract void setAttacks();

    public void resetAttack() {
        isAttByWhite = false;
        isAttByBlack = false;
    }

    public boolean isAttByOponent (String myColor) {
        if (myColor.equalsIgnoreCase("White") && isAttByBlack)
            return true;

        return myColor.equalsIgnoreCase("Black") && isAttByWhite;
    }

    public void setAttackedBy(String Color) {
        if (Color.equalsIgnoreCase("White"))
            isAttByWhite = true;
        else
            isAttByBlack = true;
    }

    public boolean isAttByWhite() {
        return isAttByWhite;
    }

    public boolean isAttByBlack() {
        return isAttByBlack;
    }

    //Getters and Setters
    public void setMoved(boolean cond) {
        Moved = cond;
    }

    public boolean isMoved() {
        return Moved;
    }

    public Image getImage() {
        return this.Icon.getImage();
    }

    public int getPositionX() {
        return Position[0];
    }

    public int getPositionY() {
        return Position[1];
    }

    public void setPosition(int x, int y) {
        Position[0] = x;
        Position[1] = y;
    }

    public String getColor() {
        return Color;
    }

}
