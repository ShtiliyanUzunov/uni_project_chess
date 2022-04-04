package chess.figures;

import java.lang.Math;
import java.util.List;

import javax.swing.ImageIcon;

import chess.Board;
import chess.util.Move;
import chess.util.Patterns;

import static chess.util.Patterns.diagonalPattern;

public class Bishop extends Figure {

    private static final ImageIcon iconWhite = new ImageIcon(
            "images\\white_set\\Bishop.png");
    private static final ImageIcon iconBlack = new ImageIcon(
            "images\\black_set\\Bishop.png");

    public Bishop(String color, int x, int y, Board b) {
        super.color = color;
        super.position[0] = x;
        super.position[1] = y;
        super.board = b;

        if (isWhite())
            this.icon = iconWhite;
        else
            this.icon = iconBlack;
    }

    @Override
    public void markAttacks() {
        Patterns.selectUsingDiagonalPatternFromPosition(this.getX(), this.getY())
                .forEach(fig -> board.attacked(fig.getX(), fig.getY(), this.getColor()));
    }

    @Override
    public List<Move> getAvailableMoves() {
        return getAvailableMovesFromPattern(diagonalPattern);
    }

    @Override
    public String getShortName() {
        return "B";
    }

    @Override
    public int getMaterialValue() {
        return 3;
    }

    @Override
    public boolean isTargetLocationValid(int x, int y) {
        return Math.abs(x - this.getX()) == Math
                .abs(y - this.getY());
    }
}
