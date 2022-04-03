package chess.figures;

import java.lang.Math;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;

import chess.Board;
import chess.util.Move;

import static chess.util.Patterns.kingMovePattern;

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
    public void markAttacks() {
        Arrays.stream(kingMovePattern).forEach((pattern) -> {
            board.attacked(this.getX() + pattern[0], this.getY() + pattern[1], this.getColor());
        });

    }

    @Override
    public List<Move> getAvailableMoves() {
        //Add casteling moves to pattern.
        List<int[]> moves = Arrays.stream(kingMovePattern).collect(Collectors.toList());
        moves.add(new int[]{2, 0});
        moves.add(new int[]{-2, 0});

        return getAvailableMovesFromPattern(moves.toArray(new int[][]{}));
    }

    @Override
    public String getShortName() {
        return "Kg";
    }

    @Override
    public int getMaterialValue() {
        return 0;
    }

    @Override
    public boolean isTargetLocationValid(int x, int y) {
        boolean moveX = (Math.abs(x - this.getX()) == 1) && (y == this.getY());
        boolean moveY = (Math.abs(y - this.getY()) == 1) && (x == this.getX());
        boolean moveXY = (Math.abs(x - this.getX()) == 1) && (Math.abs(y - this.getY()) == 1);
        return moveX || moveY || moveXY;
    }


}
