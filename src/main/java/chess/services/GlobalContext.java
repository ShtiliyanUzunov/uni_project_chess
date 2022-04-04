package chess.services;

import chess.Board;
import chess.util.Configuration;

public class GlobalContext {

    private static Board board;
    private static BoardMovement boardMovement;
    private static GameServices gameServices;
    private static Configuration configuration;

    static {
        boardMovement = new BoardMovement();
        board = new Board();
        board.initializeBoard();
        gameServices = new GameServices();
        configuration = new Configuration();
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

    public static Configuration getConfiguration() {
        return configuration;
    }

}
