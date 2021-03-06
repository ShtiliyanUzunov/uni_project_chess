package chess;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import chess.figures.*;
import chess.services.History;
import chess.services.Logging;
import chess.util.*;
import communication.ChannelNames;
import communication.EventBus;
import lombok.Getter;
import lombok.Setter;

public class Board implements Serializable {

    private final Figure[][] chessBoard = new Figure[8][8];

    @Getter
    private Figure whiteKing;
    @Getter
    private Figure blackKing;

    @Getter
    @Setter
    private int[] lastMove;

    @Getter
    @Setter
    private String playerTurn;

    @Getter
    private List<Move> availableMovesForPlayer;

    @Getter
    @Setter
    private GameStatus gameStatus = GameStatus.RUNNING;

    @Getter
    private final History history = new History();

    private static final EventBus eventBus = EventBus.getEventBus();

    public Board() {
        eventBus.register(ChannelNames.MOVE_FINISHED, this::onMoveFinished);
        eventBus.register(ChannelNames.HISTORY_BACKWARD, history::backwards);
        eventBus.register(ChannelNames.HISTORY_FORWARD, history::forward);
        eventBus.register(ChannelNames.HISTORY_MOVE_TO_START, history::moveToStart);
    }

    private Void onMoveFinished(Object obj) {
        this.markBoardAttacks();
        availableMovesForPlayer = new ArrayList<>();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                availableMovesForPlayer.addAll(chessBoard[i][j].getAvailableMoves());

        long end = System.currentTimeMillis();
        Logging.log("Available moves calc time: " + (end - start));

        updateGameStatusAndSendNotifications();

        return null;
    }

    private void updateGameStatusAndSendNotifications() {
        if (kingChecked() && !checkMate()) {
            EventBus.getEventBus().post(ChannelNames.CHECK, null);
        }

        if (checkMate()) {
            gameStatus = GameStatus.CHECKMATE;
            EventBus.getEventBus().post(ChannelNames.CHECKMATE, null);
        }

        if (stalemate()) {
            gameStatus = GameStatus.STALEMATE;
            EventBus.getEventBus().post(ChannelNames.STALEMATE, null);
        }

        if (insufficientMaterial()) {
            gameStatus = GameStatus.INSUFFICIENT_MATERIAL;
        }

        EventBus.getEventBus().post(ChannelNames.UI_INFO_UPDATE, null);
    }

    @PureFunction
    public boolean isGameOver() {
        return checkMate() || stalemate() || insufficientMaterial();
    }

    @PureFunction
    public boolean kingChecked() {
        Figure king = getKingInTurn();

        return king.isAttByOpponent();
    }

    @PureFunction
    public boolean isWhiteTurn() {
        return playerTurn.equalsIgnoreCase("white");
    }

    @PureFunction
    public boolean insufficientMaterial() {
        //TODO: Add additional rules for insufficient material. E.g. - 1 knight/1 Bishop each side
        int whiteMaterial = Arrays.stream(chessBoard).flatMap(x -> Arrays.stream(x).filter(
                fig -> !(fig instanceof Field) && fig.isWhite()))
                .map(Figure::getMaterialValue)
                .reduce(0, Integer::sum);

        int blackMaterial = Arrays.stream(chessBoard).flatMap(x -> Arrays.stream(x).filter(
                fig -> !(fig instanceof Field) && fig.isBlack()))
                .map(Figure::getMaterialValue)
                .reduce(0, Integer::sum);

        boolean insufficient = whiteMaterial == 0 && blackMaterial == 0;

        if (insufficient) {
            eventBus.post(ChannelNames.INSUFFICIENT_MATERIAL ,null);
        }

        return insufficient;
    }

    @PureFunction
    public Figure getKingInTurn() {
        return isWhiteTurn() ? getWhiteKing() : getBlackKing();
    }

    private boolean checkMate() {
        return kingChecked() && availableMovesForPlayer.size() == 0;
    }

    private boolean stalemate() {
        return !kingChecked() && availableMovesForPlayer.size() == 0;
    }

    // Main Initializing Function
    public void initializeBoard() {
        lastMove = new int[4];
        playerTurn = "White";
        history.clear();
        gameStatus = GameStatus.RUNNING;

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
        whiteKing = new King("White", 4, 0, this);
        chessBoard[4][0] = whiteKing;

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
        blackKing = new King("Black", 4, 7, this);
        chessBoard[4][7] = blackKing;

        /* EMPTY FIELDS */
        for (int x = 0; x < 8; x++) {
            for (int y = 2; y < 6; y++) {
                chessBoard[x][y] = new Field();
            }
        }

        history.saveState();
        eventBus.post(ChannelNames.MOVE_FINISHED, null);
    }

    private void nullBoardAttack() {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                chessBoard[i][j].nullAttack();
    }

    public void markBoardAttacks() {
        nullBoardAttack();

        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                if (chessBoard[i][j] instanceof Field)
                    continue;
                chessBoard[i][j].markAttacks();
            }
    }

    public void attacked(int x, int y, String color) {
        try {
            chessBoard[x][y].setAttackedBy(color);
        } catch (Exception ignored) {
        }
    }

    // Getter and Setter
    public Figure getElementAt(int x, int y) {
        return chessBoard[x][y];
    }

    public void setElementAt(int x, int y, Figure fig) {
        chessBoard[x][y] = fig;
        fig.setPosition(x, y);
    }

    public int[] calculateMaterial() {
        int matWhite = 0;
        int matBlack = 0;
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Figure f = chessBoard[i][j];

                if (f instanceof Field)
                    continue;

                if (f.isWhite()) {
                    matWhite += f.getMaterialValue();
                } else {
                    matBlack += f.getMaterialValue();
                }
            }
        }
        return new int[]{matWhite, matBlack};
    }

    public EncodedBoard getEncodedState() {
        Integer[][] encodings = Arrays.stream(chessBoard).map(x -> Arrays.stream(x)
                .map(Figure::getEncoding)
                .toArray(Integer[]::new)
        ).toArray(Integer[][]::new);

        return EncodedBoard.builder()
                .encodedBoard(encodings)
                .whiteKingCoordinates(Arrays.copyOf(getWhiteKing().getPosition(), 2))
                .blackKingCoordinates(Arrays.copyOf(getBlackKing().getPosition(), 2))
                .lastMove(Arrays.copyOf(lastMove, lastMove.length))
                .playerTurn(playerTurn)
                .gameStatus(gameStatus)
                .material(calculateMaterial())
                .build();
    }

    public void restoreFromEncodedState(EncodedBoard encodedBoard) {
        setLastMove(encodedBoard.getLastMove());
        setPlayerTurn(encodedBoard.getPlayerTurn());

        Integer[][] encodings = encodedBoard.getEncodedBoard();
        for (int i = 0; i < encodings.length; i++) {
            for (int j = 0; j < encodings[i].length; j++) {
                setElementAt(i, j, FigureEncodings.getFigureFromEncoding(encodings[i][j], this));
            }
        }

        int[] wkCoordinates = encodedBoard.getWhiteKingCoordinates();
        int[] bkCoordinates = encodedBoard.getBlackKingCoordinates();
        whiteKing = getElementAt(wkCoordinates[0], wkCoordinates[1]);
        blackKing = getElementAt(bkCoordinates[0], bkCoordinates[1]);
        gameStatus = encodedBoard.getGameStatus();
    }
}
