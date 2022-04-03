package chess.figures;

import javax.swing.ImageIcon;
import chess.Board;
import chess.util.Move;
import chess.util.Patterns;

import java.util.List;

import static chess.util.Patterns.horizontalAndVerticalPattern;


public class Rook extends Figure {

    private static final ImageIcon iconWhite = new ImageIcon(
            "images\\white_set\\Rook.png");
    private static final ImageIcon iconBlack = new ImageIcon(
            "images\\black_set\\Rook.png");

    public Rook(String Col, int x, int y, Board b) {
        moved = false;

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
        Patterns.selectUsingHorizontalAndVerticalPatternFromPosition(this.getX(), this.getY())
                .forEach(fig -> board.attacked(fig.getX(), fig.getY(), this.getColor()));
    }

    @Override
    public List<Move> getAvailableMoves() {
        return getAvailableMovesFromPattern(horizontalAndVerticalPattern);
    }

    @Override
    public String getShortName() {
        return "R";
    }

    @Override
    public int getMaterialValue() {
        return 5;
    }

    @Override
    public boolean isTargetLocationValid(int x, int y) {
        boolean checkX = x == this.getX();
        boolean checkY = y == this.getY();
        return checkX || checkY;
    }
}
