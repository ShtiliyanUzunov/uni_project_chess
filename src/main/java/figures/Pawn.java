package figures;
import javax.swing.ImageIcon;

import chess.Board;
import chess.GlobalState;

public class Pawn extends Figure {

	private static final ImageIcon iconWhite = new ImageIcon(
			"images\\white_set\\Pawn.png");
	private static final ImageIcon iconBlack = new ImageIcon(
			"images\\black_set\\Pawn.png");

	public Pawn(String Col, int x, int y, Board b) {
		this.color = Col;

		if (Col.equalsIgnoreCase("white"))
			this.icon = iconWhite;
		else
			this.icon = iconBlack;

		this.position[0] = x;
		this.position[1] = y;
		super.board = b;
	}

	public void setAttacks(){
		if(Pawn.this.color.equalsIgnoreCase("White")){
			board.Attacked(Pawn.this.getX()+1, Pawn.this.getY()+1,"White");
			board.Attacked(Pawn.this.getX()-1, Pawn.this.getY()+1,"White");
		}
		else
		{
			board.Attacked(Pawn.this.getX()+1, Pawn.this.getY()-1,"Black");
			board.Attacked(Pawn.this.getX()-1, Pawn.this.getY()-1,"Black");
		}
	}
	
	public boolean isMoveValid(int x, int y) {
		if (!(board.getElementAt(x, y) instanceof Field) && (x == Pawn.this.getX()))
			return false;

		if (Pawn.this.getColor().equalsIgnoreCase("White")) {
			// Two Forward
			if (Pawn.this.getY() == 1
					&& (Pawn.this.getY() - y) == -2
					&& (Pawn.this.getX() == x))
				return true;

			// One Forward
			if ((Pawn.this.getY() - y == -1)
					&& (Pawn.this.getX() == x))
				return true;

			// Takes
			return (Pawn.this.getY() - y == -1)
					&& (!(board.getElementAt(x, y) instanceof Field))
					&& (((Pawn.this.getX() - x) == 1) || ((Pawn.this.getX() - x) == -1));
		}else{
			//Black
			// Two Forward
						if (Pawn.this.getY() == 6
								&& (Pawn.this.getY() - y) == 2
								&& (Pawn.this.getX() == x))
							return true;

						// One Forward
						if ((Pawn.this.getY() - y == 1)
								&& (Pawn.this.getX() == x))
							return true;

						// Takes
			return (Pawn.this.getY() - y == 1)
					&& (!(board.getElementAt(x, y) instanceof Field))
					&& (((Pawn.this.getX() - x) == 1) || ((Pawn.this.getX() - x) == -1));
		}
	}
}
