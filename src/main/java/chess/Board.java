package chess;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import chess.figures.*;
import chess.util.Move;
import chess.util.PureFunction;
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

    private static final EventBus eventBus = EventBus.getEventBus();

    public Board() {
        eventBus.register(ChannelNames.MOVE_FINISHED, this::onMoveFinished);
    }

    private Void onMoveFinished(Object obj) {
        this.markBoardAttacks();
        availableMovesForPlayer = new ArrayList<>();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                availableMovesForPlayer.addAll(chessBoard[i][j].getAvailableMoves());
        long end = System.currentTimeMillis();
        System.out.println("Available moves calc: " + (end - start));

        if (checkMate()) {
            EventBus.getEventBus().post(ChannelNames.UI_CHECKMATE, null);
        }

        if (kingChecked() && !checkMate()) {
            EventBus.getEventBus().post(ChannelNames.UI_CHECK, null);
        }

        if (stalemate()) {
            EventBus.getEventBus().post(ChannelNames.UI_STALEMATE, null);
        }

        EventBus.getEventBus().post(ChannelNames.UI_INFO_UPDATE, null);
        return null;
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
    public boolean insufficientMaterial() {
        //TODO: Add additional rules for insufficient material. E.g. - 1 knight/1 Bishop each side
        int whiteMaterial = Arrays.stream(chessBoard).flatMap(x -> Arrays.stream(x).filter(
                fig -> !(fig instanceof Field) && fig.getColor().equalsIgnoreCase("white")))
                .map(Figure::getMaterialValue)
                .reduce(0, Integer::sum);

        int blackMaterial = Arrays.stream(chessBoard).flatMap(x -> Arrays.stream(x).filter(
                fig -> !(fig instanceof Field) && fig.getColor().equalsIgnoreCase("black")))
                .map(Figure::getMaterialValue)
                .reduce(0, Integer::sum);

        return whiteMaterial == 0 && blackMaterial == 0;
    }

    @PureFunction
    public Figure getKingInTurn() {
        return getPlayerTurn().equalsIgnoreCase("white") ?
                getWhiteKing() : getBlackKing();
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

                if (f.getColor().equalsIgnoreCase("white")) {
                    matWhite += f.getMaterialValue();
                } else {
                    matBlack += f.getMaterialValue();
                }
            }
        }
        return new int[]{matWhite, matBlack};
    }
}
