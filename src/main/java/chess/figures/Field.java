package chess.figures;

public class Field extends Figure {

    public void setAttacks() {
    }

    public boolean isMoveValid(int x, int y) {

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
