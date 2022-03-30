package chess.figures;

import javax.swing.ImageIcon;
import chess.Board;


public class Rook extends Figure {

    private static final ImageIcon iconWhite = new ImageIcon(
            "images\\white_set\\Rook.png");
    private static final ImageIcon iconBlack = new ImageIcon(
            "images\\black_set\\Rook.png");

    public Rook(String Col, int x, int y, Board b) {
        moved = false;

        if (Col.equalsIgnoreCase("white"))
            this.icon = iconWhite;
        else
            this.icon = iconBlack;

        super.color = Col;
        super.position[0] = x;
        super.position[1] = y;
        super.board = b;
    }

    @Override
    public void setAttacks() {
        Patterns.applyHorizontalAndVerticalPatternFromPosition(this.getX(), this.getY(), this.getColor());
    }

    @Override
    public boolean isTargetLocationValid(int x, int y) {
        boolean checkX = x == this.getX();
        boolean checkY = y == this.getY();
        return checkX || checkY;
    }
}
