package chess.figures;

public class Field extends Figure {

    @Override
    public void setAttacks() { }

    @Override
    public boolean isTargetLocationValid(int x, int y) {
        return false;
    }

    public Field() {
        icon = null;
        color = null;
        position = null;
        isAttByWhite = false;
        isAttByBlack = false;
    }
}
