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
			this.Icon = iconWhite;
		else
			this.Icon = iconBlack;

		super.Color = Col;
		super.Position[0] = x;
		super.Position[1] = y;
	}

	public void setAttacks(){
		Chess.Attacked(Knight.this.getPositionX()+2, Knight.this.getPositionY()+1, Knight.this.getColor());
		Chess.Attacked(Knight.this.getPositionX()+1, Knight.this.getPositionY()+2, Knight.this.getColor());
		Chess.Attacked(Knight.this.getPositionX()-2, Knight.this.getPositionY()+1, Knight.this.getColor());
		Chess.Attacked(Knight.this.getPositionX()-1, Knight.this.getPositionY()+2, Knight.this.getColor());
		Chess.Attacked(Knight.this.getPositionX()+2, Knight.this.getPositionY()-1, Knight.this.getColor());
		Chess.Attacked(Knight.this.getPositionX()+1, Knight.this.getPositionY()-2, Knight.this.getColor());
		Chess.Attacked(Knight.this.getPositionX()-2, Knight.this.getPositionY()-1, Knight.this.getColor());
		Chess.Attacked(Knight.this.getPositionX()-1, Knight.this.getPositionY()-2, Knight.this.getColor());
		}
		
	
	public boolean isMoveValid(int x, int y) {

		return (Math.abs(x - Knight.this.getPositionX()) == 1 && Math.abs(y
				- Knight.this.getPositionY()) == 2)
				|| (Math.abs(x - Knight.this.getPositionX()) == 2 && Math.abs(y
				- Knight.this.getPositionY()) == 1);
	}

}
