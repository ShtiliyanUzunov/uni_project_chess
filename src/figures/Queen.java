package figures;

import javax.swing.ImageIcon;

import chess.Chess;

public class Queen extends Figure {

	private static final ImageIcon iconWhite = new ImageIcon(
			"images\\white_set\\Queen.png");
	private static final ImageIcon iconBlack = new ImageIcon(
			"images\\black_set\\Queen.png");

	public Queen(String Col, int x, int y) {
		if (Col.equalsIgnoreCase("white"))
			this.Icon = iconWhite;
		else
			this.Icon = iconBlack;

		super.Color = Col;
		super.Position[0] = x;
		super.Position[1] = y;
	}

	public void setAttacks() {
		String col = Queen.this.getColor();

		if (Queen.this.getPositionX() != 7)
			for (int i = Queen.this.getPositionX(); i < 7; i++) {
				if (!(Chess.getElementAt(i + 1, Queen.this.getPositionY()) instanceof Field)) {
					Chess.getElementAt(i + 1, Queen.this.getPositionY())
							.setAttackedBy(col);
					break;
				}
				
				Chess.getElementAt(i + 1, Queen.this.getPositionY())
						.setAttackedBy(col);
			}

		if (Queen.this.getPositionY() != 7)
			for (int i = Queen.this.getPositionY(); i < 7; i++) {
				if (!(Chess.getElementAt(Queen.this.getPositionX(), i + 1) instanceof Field)) {
					Chess.getElementAt(Queen.this.getPositionX(), i + 1)
							.setAttackedBy(col);
					break;
				}
				Chess.getElementAt(Queen.this.getPositionX(), i + 1)
						.setAttackedBy(col);
			}

		if (Queen.this.getPositionX() != 0)
			for (int i = Queen.this.getPositionX(); i > 0; i--) {
				if (!(Chess.getElementAt(i - 1, Queen.this.getPositionY()) instanceof Field)) {
					Chess.getElementAt(i - 1, Queen.this.getPositionY())
							.setAttackedBy(col);
					break;
				}
				Chess.getElementAt(i - 1, Queen.this.getPositionY())
						.setAttackedBy(col);
			}

		if (Queen.this.getPositionY() != 0)
			for (int i = Queen.this.getPositionY(); i > 0; i--) {
				if (!(Chess.getElementAt(Queen.this.getPositionX(), i - 1) instanceof Field)) {
					Chess.getElementAt(Queen.this.getPositionX(), i - 1)
							.setAttackedBy(col);
					break;
				}
				Chess.getElementAt(Queen.this.getPositionX(), i - 1)
						.setAttackedBy(col);
			}

		if (Queen.this.getPositionY() != 0 && Queen.this.getPositionX() != 7)
			for (int x = Queen.this.getPositionX(), y = Queen.this
					.getPositionY(); y > 0 && x < 7; y--, x++) {
				if (!(Chess.getElementAt(x + 1, y - 1) instanceof Field)) {
					Chess.getElementAt(x + 1, y - 1).setAttackedBy(col);
					break;
				}
				Chess.getElementAt(x + 1, y - 1).setAttackedBy(col);
			}

		if (Queen.this.getPositionY() != 0 && Queen.this.getPositionX() != 0)
			for (int x = Queen.this.getPositionX(), y = Queen.this
					.getPositionY(); y > 0 && x > 0; y--, x--) {
				if (!(Chess.getElementAt(x - 1, y - 1) instanceof Field)) {
					Chess.getElementAt(x - 1, y - 1).setAttackedBy(col);
					break;
				}
				Chess.getElementAt(x - 1, y - 1).setAttackedBy(col);
			}

		if (Queen.this.getPositionY() != 7 && Queen.this.getPositionX() != 0)
			for (int x = Queen.this.getPositionX(), y = Queen.this
					.getPositionY(); y < 7 && x > 0; y++, x--) {
				if (!(Chess.getElementAt(x - 1, y + 1) instanceof Field)) {
					Chess.getElementAt(x - 1, y + 1).setAttackedBy(col);
					break;
				}
				Chess.getElementAt(x - 1, y + 1).setAttackedBy(col);
			}

		if (Queen.this.getPositionY() != 7 && Queen.this.getPositionX() != 7)
			for (int x = Queen.this.getPositionX(), y = Queen.this
					.getPositionY(); y < 7 && x < 7; y++, x++) {
				if (!(Chess.getElementAt(x + 1, y + 1) instanceof Field)) {
					Chess.getElementAt(x + 1, y + 1).setAttackedBy(col);
					break;
				}
				Chess.getElementAt(x + 1, y + 1).setAttackedBy(col);
			}

	}

	public boolean isMoveValid(int x, int y) {
		boolean checkX = x == Queen.this.getPositionX();
		boolean checkY = y == Queen.this.getPositionY();
		boolean checkXY = Math.abs(x - Queen.this.getPositionX()) == Math.abs(y
				- Queen.this.getPositionY());

		return checkX || checkY || checkXY;
	}

}
