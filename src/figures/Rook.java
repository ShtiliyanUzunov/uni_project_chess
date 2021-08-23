package figures;

import javax.swing.ImageIcon;

import chess.Chess;

public class Rook extends Figure {

	private static final ImageIcon iconWhite = new ImageIcon(
			"images\\white_set\\Rook.png");
	private static final ImageIcon iconBlack = new ImageIcon(
			"images\\black_set\\Rook.png");

	public Rook(String Col, int x, int y) {
		Moved = false;

		if (Col.equalsIgnoreCase("white"))
			this.Icon = iconWhite;
		else
			this.Icon = iconBlack;

		super.Color = Col;
		super.Position[0] = x;
		super.Position[1] = y;
	}
	
	public void setAttacks(){
		String col = Rook.this.getColor();
		
		if(Rook.this.getPositionX()!=7)
		for(int i=Rook.this.getPositionX();i<7;i++){
			if(!(Chess.getElementAt(i+1, Rook.this.getPositionY())instanceof Field)){
				Chess.getElementAt(i+1, Rook.this.getPositionY()).setAttackedBy(col);
				break;
			}
			Chess.getElementAt(i+1, Rook.this.getPositionY()).setAttackedBy(col);
		}
		
		if(Rook.this.getPositionY()!=7)
		for(int i=Rook.this.getPositionY();i<7;i++){
			if(!(Chess.getElementAt(Rook.this.getPositionX(), i+1)instanceof Field)){
				Chess.getElementAt(Rook.this.getPositionX(), i+1).setAttackedBy(col);
				break;
			}
			Chess.getElementAt(Rook.this.getPositionX(), i+1).setAttackedBy(col);
		}
		
		if(Rook.this.getPositionX()!=0)
		for(int i=Rook.this.getPositionX();i>0;i--){
			if(!(Chess.getElementAt(i-1, Rook.this.getPositionY())instanceof Field)){
				Chess.getElementAt(i-1, Rook.this.getPositionY()).setAttackedBy(col);
				break;
			}
			Chess.getElementAt(i-1, Rook.this.getPositionY()).setAttackedBy(col);
		}
		
		if(Rook.this.getPositionY()!=0)
		for(int i=Rook.this.getPositionY();i>0;i--){
			if(!(Chess.getElementAt(Rook.this.getPositionX(), i-1)instanceof Field)){
				Chess.getElementAt(Rook.this.getPositionX(), i-1).setAttackedBy(col);
				break;
			}
			Chess.getElementAt(Rook.this.getPositionX(), i-1).setAttackedBy(col);
		}
	}
	
	public boolean isMoveValid(int x, int y) {
		boolean checkX=x==Rook.this.getPositionX();
		boolean checkY=y==Rook.this.getPositionY();
		return checkX || checkY;
	}
}
