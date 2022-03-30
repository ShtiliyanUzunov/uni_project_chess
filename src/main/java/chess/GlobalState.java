package chess;

public class GlobalState {

    private static Board board;
    private static BoardMovement boardMovement;
    private static GameOperations gameOperations;


    static {
        board = new Board();
        board.initializeBoard();
        boardMovement = new BoardMovement();
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
