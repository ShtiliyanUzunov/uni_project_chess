package chess.figures;

import java.lang.Math;
import java.util.List;

import javax.swing.ImageIcon;

import chess.Board;
import chess.util.FigureEncodings;
import chess.util.Move;

import static chess.util.Patterns.selectUsingKnightPatternFromPosition;
import static chess.util.Patterns.knightMovePattern;

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
    public void markAttacks() {
        selectUsingKnightPatternFromPosition(this.getX(), this.getY())
                .forEach(fig -> board.attacked(fig.getX(), fig.getY(), this.getColor()));
    }

    @Override
    public List<Move> getAvailableMoves() {
        return getAvailableMovesFromPattern(knightMovePattern);
    }

    @Override
    public String getShortName() {
        return "Kn";
    }

    @Override
    public int getMaterialValue() {
        return 3;
    }

    @Override
    public int getEncoding() {
        if (isWhite())
            return FigureEncodings.WHITE_KNIGHT;
        return FigureEncodings.BLACK_KNIGHT;
    }


    @Override
    public boolean isTargetLocationValid(int x, int y) {
        return (Math.abs(x - this.getX()) == 1 && Math.abs(y
                - this.getY()) == 2)
                || (Math.abs(x - this.getX()) == 2 && Math.abs(y
                - this.getY()) == 1);
    }

}
