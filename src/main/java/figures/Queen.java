package figures;

import javax.swing.ImageIcon;

import chess.Board;
import chess.GlobalState;

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

	public void setAttacks() {
		String col = Queen.this.getColor();

		if (Queen.this.getX() != 7)
			for (int i = Queen.this.getX(); i < 7; i++) {
				if (!(board.getElementAt(i + 1, Queen.this.getY()) instanceof Field)) {
					board.getElementAt(i + 1, Queen.this.getY())
							.setAttackedBy(col);
					break;
				}
				
				board.getElementAt(i + 1, Queen.this.getY())
						.setAttackedBy(col);
			}

		if (Queen.this.getY() != 7)
			for (int i = Queen.this.getY(); i < 7; i++) {
				if (!(board.getElementAt(Queen.this.getX(), i + 1) instanceof Field)) {
					board.getElementAt(Queen.this.getX(), i + 1)
							.setAttackedBy(col);
					break;
				}
				board.getElementAt(Queen.this.getX(), i + 1)
						.setAttackedBy(col);
			}

		if (Queen.this.getX() != 0)
			for (int i = Queen.this.getX(); i > 0; i--) {
				if (!(board.getElementAt(i - 1, Queen.this.getY()) instanceof Field)) {
					board.getElementAt(i - 1, Queen.this.getY())
							.setAttackedBy(col);
					break;
				}
				board.getElementAt(i - 1, Queen.this.getY())
						.setAttackedBy(col);
			}

		if (Queen.this.getY() != 0)
			for (int i = Queen.this.getY(); i > 0; i--) {
				if (!(board.getElementAt(Queen.this.getX(), i - 1) instanceof Field)) {
					board.getElementAt(Queen.this.getX(), i - 1)
							.setAttackedBy(col);
					break;
				}
				board.getElementAt(Queen.this.getX(), i - 1)
						.setAttackedBy(col);
			}

		if (Queen.this.getY() != 0 && Queen.this.getX() != 7)
			for (int x = Queen.this.getX(), y = Queen.this
					.getY(); y > 0 && x < 7; y--, x++) {
				if (!(board.getElementAt(x + 1, y - 1) instanceof Field)) {
					board.getElementAt(x + 1, y - 1).setAttackedBy(col);
					break;
				}
				board.getElementAt(x + 1, y - 1).setAttackedBy(col);
			}

		if (Queen.this.getY() != 0 && Queen.this.getX() != 0)
			for (int x = Queen.this.getX(), y = Queen.this
					.getY(); y > 0 && x > 0; y--, x--) {
				if (!(board.getElementAt(x - 1, y - 1) instanceof Field)) {
					board.getElementAt(x - 1, y - 1).setAttackedBy(col);
					break;
				}
				board.getElementAt(x - 1, y - 1).setAttackedBy(col);
			}

		if (Queen.this.getY() != 7 && Queen.this.getX() != 0)
			for (int x = Queen.this.getX(), y = Queen.this
					.getY(); y < 7 && x > 0; y++, x--) {
				if (!(board.getElementAt(x - 1, y + 1) instanceof Field)) {
					board.getElementAt(x - 1, y + 1).setAttackedBy(col);
					break;
				}
				board.getElementAt(x - 1, y + 1).setAttackedBy(col);
			}

		if (Queen.this.getY() != 7 && Queen.this.getX() != 7)
			for (int x = Queen.this.getX(), y = Queen.this
					.getY(); y < 7 && x < 7; y++, x++) {
				if (!(board.getElementAt(x + 1, y + 1) instanceof Field)) {
					board.getElementAt(x + 1, y + 1).setAttackedBy(col);
					break;
				}
				board.getElementAt(x + 1, y + 1).setAttackedBy(col);
			}

	}

	public boolean isMoveValid(int x, int y) {
		boolean checkX = x == Queen.this.getX();
		boolean checkY = y == Queen.this.getY();
		boolean checkXY = Math.abs(x - Queen.this.getX()) == Math.abs(y
				- Queen.this.getY());

		return checkX || checkY || checkXY;
	}

}
