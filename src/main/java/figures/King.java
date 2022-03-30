package figures;

import java.lang.Math;

import javax.swing.ImageIcon;

import chess.Board;
import chess.GlobalState;

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

	
	public void setAttacks() {
		board.Attacked(King.this.getX() + 1, King.this.getY(),
				King.this.getColor());
		board.Attacked(King.this.getX(), King.this.getY() + 1,
				King.this.getColor());
		board.Attacked(King.this.getX() - 1, King.this.getY(),
				King.this.getColor());
		board.Attacked(King.this.getX(), King.this.getY() - 1,
				King.this.getColor());
		board.Attacked(King.this.getX() + 1,
				King.this.getY() + 1, King.this.getColor());
		board.Attacked(King.this.getX() + 1,
				King.this.getY() - 1, King.this.getColor());
		board.Attacked(King.this.getX() - 1,
				King.this.getY() + 1, King.this.getColor());
		board.Attacked(King.this.getX() - 1,
				King.this.getY() - 1, King.this.getColor());

	}

	public boolean isMoveValid(int x, int y) {
		boolean moveX = (Math.abs(x-King.this.getX())==1)&&(y==King.this.getY());
		boolean moveY = (Math.abs(y-King.this.getY())==1)&&(x==King.this.getX());
		boolean moveXY = (Math.abs(x-King.this.getX())==1)&&(Math.abs(y-King.this.getY())==1);
		return moveX || moveY || moveXY;
	}
	

}
