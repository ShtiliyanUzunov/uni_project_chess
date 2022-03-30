package figures;

import javax.swing.ImageIcon;

import chess.Board;
import chess.GlobalState;

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

    public void setAttacks() {
        String col = Rook.this.getColor();

        if (Rook.this.getX() != 7)
            for (int i = Rook.this.getX(); i < 7; i++) {
                if (!(board.getElementAt(i + 1, Rook.this.getY()) instanceof Field)) {
                    board.getElementAt(i + 1, Rook.this.getY()).setAttackedBy(col);
                    break;
                }
                board.getElementAt(i + 1, Rook.this.getY()).setAttackedBy(col);
            }

        if (Rook.this.getY() != 7)
            for (int i = Rook.this.getY(); i < 7; i++) {
                if (!(board.getElementAt(Rook.this.getX(), i + 1) instanceof Field)) {
                    board.getElementAt(Rook.this.getX(), i + 1).setAttackedBy(col);
                    break;
                }
                board.getElementAt(Rook.this.getX(), i + 1).setAttackedBy(col);
            }

        if (Rook.this.getX() != 0)
            for (int i = Rook.this.getX(); i > 0; i--) {
                if (!(board.getElementAt(i - 1, Rook.this.getY()) instanceof Field)) {
                    board.getElementAt(i - 1, Rook.this.getY()).setAttackedBy(col);
                    break;
                }
                board.getElementAt(i - 1, Rook.this.getY()).setAttackedBy(col);
            }

        if (Rook.this.getY() != 0)
            for (int i = Rook.this.getY(); i > 0; i--) {
                if (!(board.getElementAt(Rook.this.getX(), i - 1) instanceof Field)) {
                    board.getElementAt(Rook.this.getX(), i - 1).setAttackedBy(col);
                    break;
                }
                board.getElementAt(Rook.this.getX(), i - 1).setAttackedBy(col);
            }
    }

    public boolean isMoveValid(int x, int y) {
        boolean checkX = x == Rook.this.getX();
        boolean checkY = y == Rook.this.getY();
        return checkX || checkY;
    }
}
