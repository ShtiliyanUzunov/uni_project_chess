package chess.figures;

import chess.util.FigureEncodings;
import chess.util.Move;

import java.util.ArrayList;
import java.util.List;

public class Field extends Figure {

    @Override
    public void markAttacks() { }

    @Override
    public List<Move> getAvailableMoves() {
        return new ArrayList<>();
    }

    @Override
    public int getEncoding() {
        return FigureEncodings.EMPTY_FIELD;
    }

    @Override
    public String getShortName() {
        return null;
    }

    @Override
    public int getMaterialValue() {
        return 0;
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
