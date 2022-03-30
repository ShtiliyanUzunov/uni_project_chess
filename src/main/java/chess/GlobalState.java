package chess;

import communication.EventBus;

public class GlobalState {

    private static Board board;
    private static ChessLogics chessLogics;


    static {
        board = new Board();
        board.initializeBoard();
        chessLogics = new ChessLogics();
    }

    public static Board getBoard() {
        return board;
    }


    public static ChessLogics getChessLogics() {
        return chessLogics;
    }

}
