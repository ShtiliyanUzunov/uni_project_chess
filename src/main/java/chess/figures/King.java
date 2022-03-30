package chess.figures;

import java.lang.Math;
import java.util.Arrays;

import javax.swing.ImageIcon;

import chess.Board;

public class King extends Figure {

    private static final ImageIcon iconWhite = new ImageIcon(
            "images\\white_set\\King.png");
    private static final ImageIcon iconBlack = new ImageIcon(
            "images\\black_set\\King.png");

    public King(String Col, int x, int y, Board b) {
        if (Col.equalsIgnoreCase("white"))
            this.icon = iconWhite;
        else
            this.icon = iconBlack;

        moved = false;
        super.color = Col;
        super.position[0] = x;
        super.position[1] = y;
        super.board = b;
    }

    @Override
    public void setAttacks() {
        int[][] kingMovePattern = {
                {1, -1}, {1, 0}, {1, 1},
                {0, -1}, {0, 1},
                {-1, -1}, {-1, 0}, {-1, 1}
        };

        Arrays.stream(kingMovePattern).forEach((pattern) -> {
            board.attacked(this.getX() + pattern[0], this.getY() + pattern[1], this.getColor());
        });

    }

    @Override
    public boolean isTargetLocationValid(int x, int y) {
        boolean moveX = (Math.abs(x - this.getX()) == 1) && (y == this.getY());
        boolean moveY = (Math.abs(y - this.getY()) == 1) && (x == this.getX());
        boolean moveXY = (Math.abs(x - this.getX()) == 1) && (Math.abs(y - this.getY()) == 1);
        return moveX || moveY || moveXY;
    }


}
