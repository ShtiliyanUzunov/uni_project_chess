package figures;

import java.lang.Math;

import javax.swing.ImageIcon;

import chess.Chess;

public class Bishop extends Figure {

	private static final ImageIcon iconWhite = new ImageIcon(
			"images\\white_set\\Bishop.png");
	private static final ImageIcon iconBlack = new ImageIcon(
			"images\\black_set\\Bishop.png");

	public Bishop(String Col, int x, int y) {
		if (Col.equalsIgnoreCase("white"))
			this.Icon = iconWhite;
		else
			this.Icon = iconBlack;

		super.Color = Col;
		super.Position[0] = x;
		super.Position[1] = y;
	}

	public void setAttacks(){
		String col = Bishop.this.getColor();
		
		if(Bishop.this.getPositionY()!=0&&Bishop.this.getPositionX()!=7)
		for(int x=Bishop.this.getPositionX(),y=Bishop.this.getPositionY();y>0&&x<7;y--,x++){
			if(!(Chess.getElementAt(x+1, y-1)instanceof Field)){
				Chess.getElementAt(x+1, y-1).setAttackedBy(col);
				break;
			}
			Chess.getElementAt(x+1, y-1).setAttackedBy(col);
		}
		
		if(Bishop.this.getPositionY()!=0&&Bishop.this.getPositionX()!=0)
		for(int x=Bishop.this.getPositionX(),y=Bishop.this.getPositionY();y>0&&x>0;y--,x--){
				if(!(Chess.getElementAt(x-1, y-1)instanceof Field)){
					Chess.getElementAt(x-1, y-1).setAttackedBy(col);
					break;
				}
				Chess.getElementAt(x-1, y-1).setAttackedBy(col);
			}
		
		if(Bishop.this.getPositionY()!=7&&Bishop.this.getPositionX()!=0)
		for(int x=Bishop.this.getPositionX(),y=Bishop.this.getPositionY();y<7&&x>0;y++,x--){
				if(!(Chess.getElementAt(x-1, y+1)instanceof Field)){
					Chess.getElementAt(x-1, y+1).setAttackedBy(col);
					break;
				}
				Chess.getElementAt(x-1, y+1).setAttackedBy(col);
			}
		
		if(Bishop.this.getPositionY()!=7&&Bishop.this.getPositionX()!=7)
			for(int x=Bishop.this.getPositionX(),y=Bishop.this.getPositionY();y<7&&x<7;y++,x++){
				if(!(Chess.getElementAt(x+1, y+1)instanceof Field)){
					Chess.getElementAt(x+1, y+1).setAttackedBy(col);
					break;
				}
				Chess.getElementAt(x+1, y+1).setAttackedBy(col);
			}
	}
	
	public boolean isMoveValid(int x, int y) {
		return Math.abs(x - this.getPositionX()) == Math
				.abs(y - this.getPositionY());
	}
}
