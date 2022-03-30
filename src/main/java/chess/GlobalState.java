package chess;

public class GlobalState {

    private static Board board;
    private static ChessLogics chessLogics;
    private static GameOperations gameOperations;


    static {
        board = new Board();
        board.initializeBoard();
        chessLogics = new ChessLogics();
        gameOperations = new GameOperations();
    }

    public static Board getBoard() {
        return board;
    }

    public static void setBoard(Board b) {
        board = b;
    }

    public static ChessLogics getChessLogics() {
        return chessLogics;
    }

}
