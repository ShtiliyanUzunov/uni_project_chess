package figures;
import javax.swing.ImageIcon;

import chess.Chess;

public class Pawn extends Figure {

	private static final ImageIcon iconWhite = new ImageIcon(
			"images\\white_set\\Pawn.png");
	private static final ImageIcon iconBlack = new ImageIcon(
			"images\\black_set\\Pawn.png");

	public Pawn(String Col, int x, int y) {
		this.Color = Col;

		if (Col.equalsIgnoreCase("white"))
			this.Icon = iconWhite;
		else
			this.Icon = iconBlack;

		this.Position[0] = x;
		this.Position[1] = y;
	}

	public void setAttacks(){
		if(Pawn.this.Color.equalsIgnoreCase("White")){
			Chess.Attacked(Pawn.this.getPositionX()+1, Pawn.this.getPositionY()+1,"White");
			Chess.Attacked(Pawn.this.getPositionX()-1, Pawn.this.getPositionY()+1,"White");
		}
		else
		{
			Chess.Attacked(Pawn.this.getPositionX()+1, Pawn.this.getPositionY()-1,"Black");
			Chess.Attacked(Pawn.this.getPositionX()-1, Pawn.this.getPositionY()-1,"Black");
		}
	}
	
	public boolean isMoveValid(int x, int y) {
		if (!(Chess.getElementAt(x, y) instanceof Field) && (x == Pawn.this.getPositionX()))
			return false;

		if (Pawn.this.getColor().equalsIgnoreCase("White")) {
			// Two Forward
			if (Pawn.this.getPositionY() == 1
					&& (Pawn.this.getPositionY() - y) == -2
					&& (Pawn.this.getPositionX() == x))
				return true;

			// One Forward
			if ((Pawn.this.getPositionY() - y == -1)
					&& (Pawn.this.getPositionX() == x))
				return true;

			// Takes
			return (Pawn.this.getPositionY() - y == -1)
					&& (!(Chess.getElementAt(x, y) instanceof Field))
					&& (((Pawn.this.getPositionX() - x) == 1) || ((Pawn.this.getPositionX() - x) == -1));
		}else{
			//Black
			// Two Forward
						if (Pawn.this.getPositionY() == 6
								&& (Pawn.this.getPositionY() - y) == 2
								&& (Pawn.this.getPositionX() == x))
							return true;

						// One Forward
						if ((Pawn.this.getPositionY() - y == 1)
								&& (Pawn.this.getPositionX() == x))
							return true;

						// Takes
			return (Pawn.this.getPositionY() - y == 1)
					&& (!(Chess.getElementAt(x, y) instanceof Field))
					&& (((Pawn.this.getPositionX() - x) == 1) || ((Pawn.this.getPositionX() - x) == -1));
		}
	}
}
