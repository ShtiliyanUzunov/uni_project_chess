package chess.services;

import chess.Board;

public class GlobalContext {

    private static Board board;
    private static BoardMovement boardMovement;
    private static GameServices gameServices;


    static {
        boardMovement = new BoardMovement();
        board = new Board();
        board.initializeBoard();
        gameServices = new GameServices();
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
