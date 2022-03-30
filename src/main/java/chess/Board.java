package chess;

import java.io.*;

import javax.swing.ImageIcon;

import figures.*;

public class Board implements Serializable {

	private final Figure[][] chessBoard = new Figure[8][8];

	// Main Initializing Function
	public void initializeBoard() {

		/*
		 * White pieces
		 */

		/* PAWNS */
		for (int x = 0; x < 8; x++)
			chessBoard[x][1] = new Pawn("White", x, 1, this);

		/* ROOKS */
		chessBoard[0][0] = new Rook("White", 0, 0, this);
		chessBoard[7][0] = new Rook("White", 7, 0, this);

		/* KNIGHTS */
		chessBoard[1][0] = new Knight("White", 1, 0, this);
		chessBoard[6][0] = new Knight("White", 6, 0, this);

		/* BISHOPS */
		chessBoard[2][0] = new Bishop("White", 2, 0, this);
		chessBoard[5][0] = new Bishop("White", 5, 0, this);

		/* QUEEN */
		chessBoard[3][0] = new Queen("White", 3, 0, this);

		/* KING */
		chessBoard[4][0] = new King("White", 4, 0, this);

		/* BLACK PIECES */

		/* PAWNS */
		for (int x = 0; x < 8; x++)
			chessBoard[x][6] = new Pawn("Black", x, 6, this);

		/* ROOKS */
		chessBoard[0][7] = new Rook("Black", 0, 7, this);
		chessBoard[7][7] = new Rook("Black", 7, 7, this);

		/* KNIGHTS */
		chessBoard[1][7] = new Knight("Black", 1, 7, this);
		chessBoard[6][7] = new Knight("Black", 6, 7, this);

		/* BISHOPS */
		chessBoard[2][7] = new Bishop("Black", 2, 7, this);
		chessBoard[5][7] = new Bishop("Black", 5, 7, this);

		/* QUEEN */
		chessBoard[3][7] = new Queen("Black", 3, 7, this);

		/* KING */
		chessBoard[4][7] = new King("Black", 4, 7, this);

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
