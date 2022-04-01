package chess.figures;

import javax.swing.ImageIcon;

import chess.Board;
import chess.BoardMovement;
import chess.services.GlobalContext;
import chess.util.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static chess.util.Patterns.knightMovePattern;

public class Pawn extends Figure {

    private static final ImageIcon iconWhite = new ImageIcon(
            "images\\white_set\\Pawn.png");
    private static final ImageIcon iconBlack = new ImageIcon(
            "images\\black_set\\Pawn.png");

    public Pawn(String Col, int x, int y, Board b) {
        this.color = Col;

        if (Col.equalsIgnoreCase("white"))
            this.icon = iconWhite;
        else
            this.icon = iconBlack;

        this.position[0] = x;
        this.position[1] = y;
        super.board = b;
    }

    @Override
    public void markAttacks() {
        if (this.color.equalsIgnoreCase("White")) {
            board.attacked(this.getX() + 1, this.getY() + 1, "White");
            board.attacked(this.getX() - 1, this.getY() + 1, "White");
        } else {
            board.attacked(this.getX() + 1, this.getY() - 1, "Black");
            board.attacked(this.getX() - 1, this.getY() - 1, "Black");
        }
    }

    @Override
    public List<Move> getValidMoves() {
        BoardMovement boardMovement = GlobalContext.getBoardMovement();
        int moveVector = color.equalsIgnoreCase("white") ? 1: -1;

        int[][] pawnPattern = {
                {0, 1}, {0, 2}, {-1, 1}, {1, 1}
        };

        return Arrays.stream(pawnPattern).filter((pattern) -> {
            int xFrom = getX();
            int yFrom = getY();
            int xTo = xFrom + pattern[0];
            int yTo = yFrom + (moveVector * pattern[1]);
            return boardMovement.isMoveValid(xFrom, yFrom, xTo, yTo, false);
        }).map(pattern -> new Move(getX(), getY(), getX() + pattern[0], getY() + moveVector * pattern[1])).collect(Collectors.toList());

    }

    @Override
    public boolean isTargetLocationValid(int x, int y) {
        if (!(board.getElementAt(x, y) instanceof Field) && (x == this.getX()))
            return false;

        if (this.getColor().equalsIgnoreCase("White")) {
            // Two Forward
            if (this.getY() == 1
                    && (this.getY() - y) == -2
                    && (this.getX() == x))
                return true;

            // One Forward
            if ((this.getY() - y == -1)
                    && (this.getX() == x))
                return true;

            // Takes
            return (this.getY() - y == -1)
                    && (!(board.getElementAt(x, y) instanceof Field))
                    && (((this.getX() - x) == 1) || ((this.getX() - x) == -1));
        } else {
            //Black
            // Two Forward
            if (this.getY() == 6
                    && (this.getY() - y) == 2
                    && (this.getX() == x))
                return true;

            // One Forward
            if ((this.getY() - y == 1)
                    && (this.getX() == x))
                return true;

            // Takes
            return (this.getY() - y == 1)
                    && (!(board.getElementAt(x, y) instanceof Field))
                    && (((this.getX() - x) == 1) || ((this.getX() - x) == -1));
        }
    }
}
