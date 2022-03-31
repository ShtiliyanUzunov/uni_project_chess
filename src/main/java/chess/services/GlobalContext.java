package chess.services;

import chess.Board;
import chess.BoardMovement;

public class GlobalContext {

    private static Board board;
    private static BoardMovement boardMovement;
    private static GameOperations gameOperations;


    static {
        boardMovement = new BoardMovement();
        board = new Board();
        board.initializeBoard();
        gameOperations = new GameOperations();
    }

    public static Board getBoard() {
        return board;
    }

    public static void setBoard(Board b) {
        board = b;
    }

    public static BoardMovement getBoardMovement() {
        return boardMovement;
    }

}
