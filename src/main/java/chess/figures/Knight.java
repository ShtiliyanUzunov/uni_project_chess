package chess.figures;

import java.lang.Math;
import java.util.Arrays;

import javax.swing.ImageIcon;

import chess.Board;

public class Knight extends Figure {

    private static final ImageIcon iconWhite = new ImageIcon(
            "images\\white_set\\Knight.png");
    private static final ImageIcon iconBlack = new ImageIcon(
            "images\\black_set\\Knight.png");


    public Knight(String Col, int x, int y, Board b) {
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
		int[][] knightMovePattern = {
				{2, 1}, {2, -1},
				{1, 2}, {1, -2},
				{-1, 2}, {-1, -2},
				{-2, 1}, {-2, -1}
		};

		Arrays.stream(knightMovePattern).forEach((pattern) -> {
			board.attacked(this.getX() + pattern[0], this.getY() + pattern[1], this.getColor());
		});
    }


    @Override
    public boolean isTargetLocationValid(int x, int y) {
        return (Math.abs(x - this.getX()) == 1 && Math.abs(y
                - this.getY()) == 2)
                || (Math.abs(x - this.getX()) == 2 && Math.abs(y
                - this.getY()) == 1);
    }

}
