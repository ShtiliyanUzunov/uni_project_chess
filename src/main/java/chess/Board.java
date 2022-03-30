package chess;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.ImageIcon;

import figures.*;

public class Board {

	private final Figure[][] chessBoard = new Figure[8][8];

	// Main Initializing Function
	public void initializeBoard() {

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
	}



	// Attacking Functions
	public void nullBoardAttack() {
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				chessBoard[i][j].resetAttack();
	}

	public void setBoardAttack() {
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				if (chessBoard[i][j] instanceof Field)
					continue;
				chessBoard[i][j].setAttacks();
			}
	}

	public void Attacked(int x, int y, String color) {
		try {
			chessBoard[x][y].setAttackedBy(color);
		} catch (Exception ignored) {}
	}

	// Getter and Setter
	public Figure getElementAt(int x, int y) {
		return chessBoard[x][y];
	}

	public void setElementAt(int x, int y, Figure fig) {
		chessBoard[x][y] = fig;
	}

}
