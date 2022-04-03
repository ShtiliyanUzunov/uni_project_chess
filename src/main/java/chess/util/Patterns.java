package chess.util;

import chess.Board;
import chess.BoardMovement;
import chess.figures.Figure;
import chess.services.GlobalContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Patterns {

    public static final int[][] diagonalPattern = {
            {1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}, {6, 6}, {7, 7},
            {-1, 1}, {-2, 2}, {-3, 3}, {-4, 4}, {-5, 5}, {-6, 6}, {-7, 7},
            {1, -1}, {2, -2}, {3, -3}, {4, -4}, {5, -5}, {6, -6}, {7, -7},
            {-1, -1}, {-2, -2}, {-3, -3}, {-4, -4}, {-5, -5}, {-6, -6}, {-7, -7}
    };

    public static final int[][] horizontalAndVerticalPattern = {
            {0, -1}, {0, -2}, {0, -3}, {0, -4}, {0, -5}, {0, -6}, {0, -7},
            {-1, 0}, {-2, 0}, {-3, 0}, {-4, 0}, {-5, 0}, {-6, 0}, {-7, 0},
            {0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 5}, {0, 6}, {0, 7},
            {1, 0}, {2, 0}, {3, 0}, {4, 0}, {5, 0}, {6, 0}, {7, 0}
    };

    public static final int[][] kingMovePattern = {
            {1, -1}, {1, 0}, {1, 1},
            {0, -1}, {0, 1},
            {-1, -1}, {-1, 0}, {-1, 1}
    };

    public static final int[][] knightMovePattern = {
            {2, 1}, {2, -1},
            {1, 2}, {1, -2},
            {-1, 2}, {-1, -2},
            {-2, 1}, {-2, -1}
    };


    public static List<Figure> selectUsingHorizontalAndVerticalPatternFromPosition(int x, int y) {
        Board board = GlobalContext.getBoard();

        return Arrays.stream(horizontalAndVerticalPattern).filter((pattern) -> {
            int targetX = x + pattern[0];
            int targetY = y + pattern[1];
            return GlobalContext.getBoardMovement().isPathClear(x, y, targetX, targetY);
        }).map((position) -> board.getElementAt(x + position[0], y + position[1])).collect(Collectors.toList());
    }

    public static List<Figure> selectUsingDiagonalPatternFromPosition(int x, int y) {
        Board board = GlobalContext.getBoard();

        return Arrays.stream(diagonalPattern).filter((pattern) -> {
            int targetX = x + pattern[0];
            int targetY = y + pattern[1];

            return GlobalContext.getBoardMovement().isPathClear(x, y, targetX, targetY);
        }).map((pattern) -> board.getElementAt(x + pattern[0], y + pattern[1])).collect(Collectors.toList());
    }

    public static List<Figure> selectUsingKnightPatternFromPosition(int x, int y) {
        Board board = GlobalContext.getBoard();
        BoardMovement movement = GlobalContext.getBoardMovement();

        return Arrays.stream(knightMovePattern).filter(position -> movement.validCoordinates(x, y, x + position[0], y + position[1]))
                .map((position) -> board.getElementAt(x + position[0], y + position[1])).collect(Collectors.toList());
    }

}
