package figures;

import java.lang.Math;

import javax.swing.ImageIcon;

import chess.Chess;

public class Knight extends Figure {

	private static final ImageIcon iconWhite = new ImageIcon(
			"images\\white_set\\Knight.png");
	private static final ImageIcon iconBlack = new ImageIcon(
			"images\\black_set\\Knight.png");

	public Knight(String Col, int x, int y) {
		if (Col.equalsIgnoreCase("white"))
			this.icon = iconWhite;
		else
			this.icon = iconBlack;

		super.color = Col;
		super.position[0] = x;
		super.position[1] = y;
	}

	public void setAttacks(){
		Chess.Attacked(Knight.this.getX()+2, Knight.this.getY()+1, Knight.this.getColor());
		Chess.Attacked(Knight.this.getX()+1, Knight.this.getY()+2, Knight.this.getColor());
		Chess.Attacked(Knight.this.getX()-2, Knight.this.getY()+1, Knight.this.getColor());
		Chess.Attacked(Knight.this.getX()-1, Knight.this.getY()+2, Knight.this.getColor());
		Chess.Attacked(Knight.this.getX()+2, Knight.this.getY()-1, Knight.this.getColor());
		Chess.Attacked(Knight.this.getX()+1, Knight.this.getY()-2, Knight.this.getColor());
		Chess.Attacked(Knight.this.getX()-2, Knight.this.getY()-1, Knight.this.getColor());
		Chess.Attacked(Knight.this.getX()-1, Knight.this.getY()-2, Knight.this.getColor());
		}
		
	
	public boolean isMoveValid(int x, int y) {

		return (Math.abs(x - Knight.this.getX()) == 1 && Math.abs(y
				- Knight.this.getY()) == 2)
				|| (Math.abs(x - Knight.this.getX()) == 2 && Math.abs(y
				- Knight.this.getY()) == 1);
	}

}
