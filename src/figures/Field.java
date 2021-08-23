package figures;
public class Field extends Figure {

	public void setAttacks(){}
	
	public boolean isMoveValid(int x,int y){
		
		return false;
		
	}
	public Field(){
		
		Icon=null;
		Color=null;
		Position=null;
		isAttByWhite=false;
		isAttByBlack=false;
		
	}
}
