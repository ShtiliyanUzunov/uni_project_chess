package chess.figures;

import java.lang.Math;

import javax.swing.ImageIcon;

import chess.Board;
import chess.util.Patterns;

public class Bishop extends Figure {

    private static final ImageIcon iconWhite = new ImageIcon(
            "images\\white_set\\Bishop.png");
    private static final ImageIcon iconBlack = new ImageIcon(
            "images\\black_set\\Bishop.png");

    public Bishop(String color, int x, int y, Board b) {
        if (color.equalsIgnoreCase("white"))
            this.icon = iconWhite;
        else
            this.icon = iconBlack;

        super.color = color;
        super.position[0] = x;
        super.position[1] = y;
        super.board = b;
    }

    @Override
    public void markAttacks() {
        Patterns.applyDiagonalPatternFromPosition(this.getX(), this.getY(), this.getColor());
    }

    @Override
    public boolean isTargetLocationValid(int x, int y) {
        return Math.abs(x - this.getX()) == Math
                .abs(y - this.getY());
    }
}
