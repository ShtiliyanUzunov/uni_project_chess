package chess.figures;

import javax.swing.ImageIcon;

import chess.Board;
import chess.util.Patterns;

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

	@Override
	public void markAttacks() {
		Patterns.applyDiagonalPatternFromPosition(this.getX(), this.getY(), this.getColor());
		Patterns.applyHorizontalAndVerticalPatternFromPosition(this.getX(), this.getY(), this.getColor());
	}

	@Override
	public boolean isTargetLocationValid(int x, int y) {
		boolean checkX = x == this.getX();
		boolean checkY = y == this.getY();
		boolean checkXY = Math.abs(x - this.getX()) == Math.abs(y
				- this.getY());

		return checkX || checkY || checkXY;
	}

}
