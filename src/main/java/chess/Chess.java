package chess;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.ImageIcon;

import figures.*;

public class Chess {

	private static Figure[][] chessBoard = new Figure[8][8];

	// Main Initializing Function
	public static void initializeBoard() {

		/*
		 * White pieces
		 */

		/* PAWNS */
		for (int x = 0; x < 8; x++)
			chessBoard[x][1] = new Pawn("White", x, 1);

		/* ROOKS */
		chessBoard[0][0] = new Rook("White", 0, 0);
		chessBoard[7][0] = new Rook("White", 7, 0);

		/* KNIGHTS */
		chessBoard[1][0] = new Knight("White", 1, 0);
		chessBoard[6][0] = new Knight("White", 6, 0);

		/* BISHOPS */
		chessBoard[2][0] = new Bishop("White", 2, 0);
		chessBoard[5][0] = new Bishop("White", 5, 0);

		/* QUEEN */
		chessBoard[3][0] = new Queen("White", 3, 0);

		/* KING */
		chessBoard[4][0] = new King("White", 4, 0, new ImageIcon(
				"images\\white_set\\King.png"));

		/* BLACK PIECES */

		/* PAWNS */
		for (int x = 0; x < 8; x++)
			chessBoard[x][6] = new Pawn("Black", x, 6);

		/* ROOKS */
		chessBoard[0][7] = new Rook("Black", 0, 7);
		chessBoard[7][7] = new Rook("Black", 7, 7);

		/* KNIGHTS */
		chessBoard[1][7] = new Knight("Black", 1, 7);
		chessBoard[6][7] = new Knight("Black", 6, 7);

		/* BISHOPS */
		chessBoard[2][7] = new Bishop("Black", 2, 7);
		chessBoard[5][7] = new Bishop("Black", 5, 7);

		/* QUEEN */
		chessBoard[3][7] = new Queen("Black", 3, 7);

		/* KING */
		chessBoard[4][7] = new King("Black", 4, 7, new ImageIcon(
				"images\\black_set\\King.png"));

		/* EMPTY FIELDS */
		for (int x = 0; x < 8; x++) {
			for (int y = 2; y < 6; y++) {
				chessBoard[x][y] = new Field();
			}
		}
		setBoardAttack();
		initializeComponents();
	}

	private static void initializeComponents() {
		ChessLogics.setLastMove(new int[4]);
		ChessLogics.setPlayerTurn("White");
		ChessLogics.setCheckIsSet(false);
		int[] whiteKing = {4,0};
		int[] blackKing = {4,7};
		ChessLogics.setWhiteKing(whiteKing);
		ChessLogics.setBlackKing(blackKing);
	}

	// Attacking Functions
	public static void nullBoardAttack() {
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				chessBoard[i][j].resetAttack();
	}

	public static void setBoardAttack() {
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				if (chessBoard[i][j] instanceof Field)
					continue;
				chessBoard[i][j].setAttacks();
			}
	}

	public static void Attacked(int x, int y, String color) {
		try {
			chessBoard[x][y].setAttackedBy(color);
		} catch (Exception ignored) {}
	}

	// Getter and Setter
	public static Figure getElementAt(int x, int y) {
		return chessBoard[x][y];
	}
	
	public static void saveGame(File outputPath){
		FileOutputStream fos;
		ObjectOutputStream oos;
		try {
			fos = new FileOutputStream(outputPath);
			oos = new ObjectOutputStream(fos);
			
			oos.writeObject(chessBoard);
			oos.writeObject(ChessLogics.getLastMove());
			oos.writeObject(ChessLogics.getPlayerTurn());
			oos.writeObject(ChessLogics.getWhiteKing());
			oos.writeObject(ChessLogics.getBlackKing());
			oos.writeBoolean(ChessLogics.isCheckIsSet());
			oos.close();
			fos.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void loadGame(File inputPath){
		FileInputStream fis;
		ObjectInputStream ois;
		try {
			fis = new FileInputStream(inputPath);
			ois = new ObjectInputStream(fis);
			
			chessBoard = (Figure[][])ois.readObject();
			ChessLogics.setLastMove((int[])ois.readObject());
			ChessLogics.setPlayerTurn((String)ois.readObject());
			ChessLogics.setWhiteKing((int[])ois.readObject());
			ChessLogics.setBlackKing((int[])ois.readObject());
			ChessLogics.setCheckIsSet(ois.readBoolean());
			fis.close();
			ois.close();
			
//			System.out.println(ChessLogics.getPlayerTurn());
//			System.out.println(ChessLogics.getWhiteKing()[0]+" "+ChessLogics.getWhiteKing()[1]);
//			System.out.println(ChessLogics.getLastMove()[0]+" "+ChessLogics.getLastMove()[1]);
//			System.out.println(ChessLogics.isCheckIsSet());
//			System.out.println(Chess.getElementAt(3, 1).getColor());
//			System.out.println(Chess.getElementAt(3, 1).getClass().toString());
//			System.out.println(Chess.getElementAt(3, 1).isAttByBlack());
//			System.out.println(Chess.getElementAt(6, 7).getPositionX()+" "+Chess.getElementAt(6, 7).getPositionY());

			
		} catch (ClassNotFoundException | IOException e) {

			e.printStackTrace();
		}

	}

	public static void setElementAt(int x, int y, Figure fig) {
		chessBoard[x][y] = fig;
	}

}
