package figures;

import java.lang.Math;

import javax.swing.ImageIcon;

import chess.Chess;

public class King extends Figure {

	public King(String Col, int x, int y, ImageIcon i) {
		Moved = false;
		super.Icon = i;
		super.Color = Col;
		super.Position[0] = x;
		super.Position[1] = y;
	}
	
	public void setAttacks() {
		Chess.Attacked(King.this.getPositionX() + 1, King.this.getPositionY(),
				King.this.getColor());
		Chess.Attacked(King.this.getPositionX(), King.this.getPositionY() + 1,
				King.this.getColor());
		Chess.Attacked(King.this.getPositionX() - 1, King.this.getPositionY(),
				King.this.getColor());
		Chess.Attacked(King.this.getPositionX(), King.this.getPositionY() - 1,
				King.this.getColor());
		Chess.Attacked(King.this.getPositionX() + 1,
				King.this.getPositionY() + 1, King.this.getColor());
		Chess.Attacked(King.this.getPositionX() + 1,
				King.this.getPositionY() - 1, King.this.getColor());
		Chess.Attacked(King.this.getPositionX() - 1,
				King.this.getPositionY() + 1, King.this.getColor());
		Chess.Attacked(King.this.getPositionX() - 1,
				King.this.getPositionY() - 1, King.this.getColor());

	}

	public boolean isMoveValid(int x, int y) {
		boolean moveX = (Math.abs(x-King.this.getPositionX())==1)&&(y==King.this.getPositionY());
		boolean moveY = (Math.abs(y-King.this.getPositionY())==1)&&(x==King.this.getPositionX());	
		boolean moveXY = (Math.abs(x-King.this.getPositionX())==1)&&(Math.abs(y-King.this.getPositionY())==1);
		return moveX || moveY || moveXY;
	}
	

}
