package chess.util;

import chess.Board;
import chess.figures.*;

public class FigureEncodings {

    public static final int EMPTY_FIELD = 0;

    public static final int WHITE_KING = 10;
    public static final int WHITE_PAWN = 11;
    public static final int WHITE_KNIGHT = 12;
    public static final int WHITE_BISHOP = 13;
    public static final int WHITE_ROOK = 14;
    public static final int WHITE_QUEEN = 15;

    public static final int BLACK_KING = 20;
    public static final int BLACK_PAWN = 21;
    public static final int BLACK_KNIGHT = 22;
    public static final int BLACK_BISHOP = 23;
    public static final int BLACK_ROOK = 24;
    public static final int BLACK_QUEEN = 25;

    public static Figure getFigureFromEncoding(Integer encoding, Board b) {
        switch (encoding) {
            case EMPTY_FIELD:
                return new Field();
            case WHITE_KING:
                return new King("White", 0, 0, b);
            case WHITE_PAWN:
                return new Pawn("White", 0, 0, b);
            case WHITE_KNIGHT:
                return new Knight("White", 0, 0, b);
            case WHITE_BISHOP:
                return new Bishop("White", 0, 0, b);
            case WHITE_ROOK:
                return new Rook("White", 0, 0, b);
            case WHITE_QUEEN:
                return new Queen("White", 0, 0, b);

            case BLACK_KING:
                return new King("Black", 0, 0, b);
            case BLACK_PAWN:
                return new Pawn("Black", 0, 0, b);
            case BLACK_KNIGHT:
                return new Knight("Black", 0, 0, b);
            case BLACK_BISHOP:
                return new Bishop("Black", 0, 0, b);
            case BLACK_ROOK:
                return new Rook("Black", 0, 0, b);
            case BLACK_QUEEN:
                return new Queen("Black", 0, 0, b);
        }

        throw new IllegalArgumentException("Encoding not supported");
    }
}
