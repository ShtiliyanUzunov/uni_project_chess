package chess.figures;

import java.lang.Math;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;

import chess.Board;
import chess.BoardMovement;
import chess.services.GlobalContext;
import chess.util.Move;
import chess.util.Patterns;

import static chess.util.Patterns.diagonalPattern;

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
        Patterns.selectUsingDiagonalPatternFromPosition(this.getX(), this.getY())
                .forEach(fig -> board.attacked(fig.getX(), fig.getY(), this.getColor()));
    }

    @Override
    public List<Move> getAvailableMoves() {
        BoardMovement boardMovement = GlobalContext.getBoardMovement();

        return Arrays.stream(diagonalPattern).filter((pattern) -> {
            int xFrom = getX();
            int yFrom = getY();
            int xTo = xFrom + pattern[0];
            int yTo = yFrom + pattern[1];
            return boardMovement.isMoveValid(xFrom, yFrom, xTo, yTo, false);
        }).map(pattern -> new Move(getX(), getY(), getX() + pattern[0], getY() + pattern[1], this.getShortName())).collect(Collectors.toList());
    }

    @Override
    public String getShortName() {
        return "B";
    }

    @Override
    public boolean isTargetLocationValid(int x, int y) {
        return Math.abs(x - this.getX()) == Math
                .abs(y - this.getY());
    }
}
