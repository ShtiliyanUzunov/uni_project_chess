package figures;

import java.lang.Math;

import javax.swing.ImageIcon;

import chess.Chess;

public class King extends Figure {

	public King(String Col, int x, int y, ImageIcon i) {
		moved = false;
		super.icon = i;
		super.color = Col;
		super.position[0] = x;
		super.position[1] = y;
	}
	
	public void setAttacks() {
		Chess.Attacked(King.this.getX() + 1, King.this.getY(),
				King.this.getColor());
		Chess.Attacked(King.this.getX(), King.this.getY() + 1,
				King.this.getColor());
		Chess.Attacked(King.this.getX() - 1, King.this.getY(),
				King.this.getColor());
		Chess.Attacked(King.this.getX(), King.this.getY() - 1,
				King.this.getColor());
		Chess.Attacked(King.this.getX() + 1,
				King.this.getY() + 1, King.this.getColor());
		Chess.Attacked(King.this.getX() + 1,
				King.this.getY() - 1, King.this.getColor());
		Chess.Attacked(King.this.getX() - 1,
				King.this.getY() + 1, King.this.getColor());
		Chess.Attacked(King.this.getX() - 1,
				King.this.getY() - 1, King.this.getColor());

	}

	public boolean isMoveValid(int x, int y) {
		boolean moveX = (Math.abs(x-King.this.getX())==1)&&(y==King.this.getY());
		boolean moveY = (Math.abs(y-King.this.getY())==1)&&(x==King.this.getX());
		boolean moveXY = (Math.abs(x-King.this.getX())==1)&&(Math.abs(y-King.this.getY())==1);
		return moveX || moveY || moveXY;
	}
	

}
