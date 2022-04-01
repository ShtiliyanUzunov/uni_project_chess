package chess.figures;

import chess.util.Move;

import java.util.ArrayList;
import java.util.List;

public class Field extends Figure {

    @Override
    public void markAttacks() { }

    @Override
    public List<Move> getValidMoves() {
        return new ArrayList<>();
    }

    @Override
    public boolean isTargetLocationValid(int x, int y) {
        return false;
    }

    public Field() {
        icon = null;
        color = null;
        position = new int[2];
        isAttByWhite = false;
        isAttByBlack = false;
    }
}
