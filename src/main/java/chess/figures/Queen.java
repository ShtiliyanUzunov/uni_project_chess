package chess.figures;

import javax.swing.ImageIcon;

import chess.Board;
import chess.util.FigureEncodings;
import chess.util.Move;
import chess.util.Patterns;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static chess.util.Patterns.*;

public class Queen extends Figure {

    private static final ImageIcon iconWhite = new ImageIcon(
            "images\\white_set\\Queen.png");
    private static final ImageIcon iconBlack = new ImageIcon(
            "images\\black_set\\Queen.png");

    public Queen(String Col, int x, int y, Board b) {
        super.color = Col;
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
        Patterns.selectUsingHorizontalAndVerticalPatternFromPosition(this.getX(), this.getY())
                .forEach(fig -> board.attacked(fig.getX(), fig.getY(), this.getColor()));
    }

    @Override
    public List<Move> getAvailableMoves() {
        List<int[]> patterns = new ArrayList<>();
        Collections.addAll(patterns, horizontalAndVerticalPattern);
        Collections.addAll(patterns, diagonalPattern);

        return getAvailableMovesFromPattern(patterns.toArray(new int[][]{}));
    }

    @Override
    public String getShortName() {
        return "Q";
    }

    @Override
    public int getMaterialValue() {
        return 9;
    }

    @Override
    public int getEncoding() {
        if (isWhite())
            return FigureEncodings.WHITE_QUEEN;
        return FigureEncodings.BLACK_QUEEN;
    }

    @Override
    public boolean isTargetLocationValid(int x, int y) {
        boolean checkX = x == this.getX();
        boolean checkY = y == this.getY();
        boolean checkXY = Math.abs(x - this.getX()) == Math.abs(y
                - this.getY());

        return checkX || checkY || checkXY;
    }

}
