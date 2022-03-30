package chess.figures;

import chess.Board;
import chess.GlobalState;

import java.util.Arrays;

public class Patterns {

    private static final int[][] diagonalPattern = {
            {1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}, {6, 6}, {7, 7},
            {-1, 1}, {-2, 2}, {-3, 3}, {-4, 4}, {-5, 5}, {-6, 6}, {-7, 7},
            {1, -1}, {2, -2}, {3, -3}, {4, -4}, {5, -5}, {6, -6}, {7, -7},
            {-1, -1}, {-2, -2}, {-3, -3}, {-4, -4}, {-5, -5}, {-6, -6}, {-7, -7}
    };

    private static final int[][] horizontalAndVerticalPattern = {
            {0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 5}, {0, 6}, {0, 7},
            {1, 0}, {2, 0}, {3, 0}, {4, 0}, {5, 0}, {6, 0}, {7, 0}
    };

    public static void applyHorizontalAndVerticalPatternFromPosition(int x, int y, String color) {
        Board board = GlobalState.getBoard();

        Arrays.stream(horizontalAndVerticalPattern).forEach((pattern) -> {
            int targetX = (x + pattern[0]) % 8;
            int targetY = (y + pattern[1]) % 8;
            if (GlobalState.getBoardMovement().isPathClear(x, y, targetX, targetY)) {
                board.attacked(targetX, targetY, color);
            }

        });
    }

    public static void applyDiagonalPatternFromPosition(int x, int y, String color) {
        Board board = GlobalState.getBoard();

        Arrays.stream(diagonalPattern).forEach((pattern) -> {
            int targetX = x + pattern[0];
            int targetY = y + pattern[1];

            if (targetX < 0 || targetX > 7 || targetY < 0 || targetY > 7) {
                return;
            }

            if (GlobalState.getBoardMovement().isPathClear(x, y, targetX, targetY)) {
                board.attacked(targetX, targetY, color);
            }
        });
    }

}
