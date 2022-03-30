package figures;

import java.lang.Math;

import javax.swing.ImageIcon;

import chess.Board;

public class Bishop extends Figure {

    private static final ImageIcon iconWhite = new ImageIcon(
            "images\\white_set\\Bishop.png");
    private static final ImageIcon iconBlack = new ImageIcon(
            "images\\black_set\\Bishop.png");

    public Bishop(String color, int x, int y) {
        if (color.equalsIgnoreCase("white"))
            this.icon = iconWhite;
        else
            this.icon = iconBlack;

        super.color = color;
        super.position[0] = x;
        super.position[1] = y;
    }

    public void setAttacks() {
        String col = Bishop.this.getColor();

        if (Bishop.this.getY() != 0 && Bishop.this.getX() != 7)
            for (int x = Bishop.this.getX(), y = Bishop.this.getY(); y > 0 && x < 7; y--, x++) {
                if (!(Board.getElementAt(x + 1, y - 1) instanceof Field)) {
                    Board.getElementAt(x + 1, y - 1).setAttackedBy(col);
                    break;
                }
                Board.getElementAt(x + 1, y - 1).setAttackedBy(col);
            }

        if (Bishop.this.getY() != 0 && Bishop.this.getX() != 0)
            for (int x = Bishop.this.getX(), y = Bishop.this.getY(); y > 0 && x > 0; y--, x--) {
                if (!(Board.getElementAt(x - 1, y - 1) instanceof Field)) {
                    Board.getElementAt(x - 1, y - 1).setAttackedBy(col);
                    break;
                }
                Board.getElementAt(x - 1, y - 1).setAttackedBy(col);
            }

        if (Bishop.this.getY() != 7 && Bishop.this.getX() != 0)
            for (int x = Bishop.this.getX(), y = Bishop.this.getY(); y < 7 && x > 0; y++, x--) {
                if (!(Board.getElementAt(x - 1, y + 1) instanceof Field)) {
                    Board.getElementAt(x - 1, y + 1).setAttackedBy(col);
                    break;
                }
                Board.getElementAt(x - 1, y + 1).setAttackedBy(col);
            }

        if (Bishop.this.getY() != 7 && Bishop.this.getX() != 7)
            for (int x = Bishop.this.getX(), y = Bishop.this.getY(); y < 7 && x < 7; y++, x++) {
                if (!(Board.getElementAt(x + 1, y + 1) instanceof Field)) {
                    Board.getElementAt(x + 1, y + 1).setAttackedBy(col);
                    break;
                }
                Board.getElementAt(x + 1, y + 1).setAttackedBy(col);
            }
    }

    public boolean isMoveValid(int x, int y) {
        return Math.abs(x - this.getX()) == Math
                .abs(y - this.getY());
    }
}
