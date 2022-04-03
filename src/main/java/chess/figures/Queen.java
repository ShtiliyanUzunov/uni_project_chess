package chess.figures;

import javax.swing.ImageIcon;

import chess.Board;
import chess.BoardMovement;
import chess.services.GlobalContext;
import chess.util.Move;
import chess.util.Patterns;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static chess.util.Patterns.diagonalPattern;
import static chess.util.Patterns.horizontalAndVerticalPattern;

public class Queen extends Figure {

    private static final ImageIcon iconWhite = new ImageIcon(
            "images\\white_set\\Queen.png");
    private static final ImageIcon iconBlack = new ImageIcon(
            "images\\black_set\\Queen.png");

    public Queen(String Col, int x, int y, Board b) {
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
        Patterns.selectUsingDiagonalPatternFromPosition(this.getX(), this.getY())
                .forEach(fig -> board.attacked(fig.getX(), fig.getY(), this.getColor()));
        Patterns.selectUsingHorizontalAndVerticalPatternFromPosition(this.getX(), this.getY())
                .forEach(fig -> board.attacked(fig.getX(), fig.getY(), this.getColor()));
    }

    @Override
    public List<Move> getAvailableMoves() {
        BoardMovement boardMovement = GlobalContext.getBoardMovement();

        List<int[]> patterns = new ArrayList<>();
        Collections.addAll(patterns, horizontalAndVerticalPattern);
        Collections.addAll(patterns, diagonalPattern);

        return patterns.stream().filter((pattern) -> {
            int xFrom = getX();
            int yFrom = getY();
            int xTo = xFrom + pattern[0];
            int yTo = yFrom + pattern[1];
            return boardMovement.isMoveValid(xFrom, yFrom, xTo, yTo, false);
        }).map(pattern -> new Move(getX(), getY(), getX() + pattern[0], getY() + pattern[1], getShortName())).collect(Collectors.toList());

    }

    @Override
    public String getShortName() {
        return "Q";
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
