package chess.services;

import chess.Board;
import chess.figures.Field;
import chess.figures.Knight;
import chess.util.PureFunction;

public class CollisionChecker {

    @PureFunction
    public boolean isPathClear(int xFrom, int yFrom, int xTo, int yTo) {
        Board board = GlobalContext.getBoard();

        if (board.getElementAt(xFrom, yFrom) instanceof Knight)
            return true;

        //Vertical movement
        if (xFrom == xTo) {
            return isVerticalPathClear(xFrom, yFrom, xTo, yTo);
        }

        //Horizontal movement
        if (yFrom == yTo) {
            return isHorizontalPathClear(xFrom, yFrom, xTo, yTo);
        }

        //Checks the 0:0 -> 7:7 Diagonal direction
        if (((xFrom > xTo) && (yFrom > yTo)) || ((xFrom < xTo) && (yFrom < yTo))) {
            return is00_77DiagonalPathClear(xFrom, yFrom, xTo, yTo);
        }

        //Checks the 7:0 -> 0:7 Diagonal direction
        if (((xFrom > xTo) && (yFrom < yTo)) || ((xFrom < xTo) && (yFrom > yTo))) {
            return is07_70DiagonalPathClear(xFrom, yFrom, xTo, yTo);
        }

        return true;
    }

    @PureFunction
    private boolean isVerticalPathClear(int xFrom, int yFrom, int xTo, int yTo) {
        Board board = GlobalContext.getBoard();
        int start, stop;

        start = Math.min(yFrom, yTo);
        stop = Math.max(yFrom, yTo);
        for (start = start + 1; start < stop; start++) {
            if (!(board.getElementAt(xFrom, start) instanceof Field))
                return false;
        }

        return true;
    }

    @PureFunction
    private boolean isHorizontalPathClear(int xFrom, int yFrom, int xTo, int yTo) {
        Board board = GlobalContext.getBoard();
        int start, stop;

        start = Math.min(xFrom, xTo);
        stop = Math.max(xFrom, xTo);
        for (start = start + 1; start < stop; start++) {
            if (!(board.getElementAt(start, yFrom) instanceof Field))
                return false;
        }

        return true;
    }

    @PureFunction
    private boolean is00_77DiagonalPathClear(int xFrom, int yFrom, int xTo, int yTo) {
        Board board = GlobalContext.getBoard();

        int xStart, xStop, yStart, yStop;

        xStart = Math.min(xFrom, xTo);
        xStop = Math.max(xFrom, xTo);
        yStart = Math.min(yFrom, yTo);
        yStop = Math.max(yFrom, yTo);

        for (xStart = xStart + 1, yStart = yStart + 1; xStart < xStop
                && yStart < yStop; xStart++, yStart++) {
            if (!(board.getElementAt(xStart, yStart) instanceof Field))
                return false;
        }

        return true;
    }

    @PureFunction
    private boolean is07_70DiagonalPathClear(int xFrom, int yFrom, int xTo, int yTo) {
        Board board = GlobalContext.getBoard();

        int xStart, xStop, yStart, yEnd;

        xStart = Math.min(xFrom, xTo);
        xStop = Math.max(xFrom, xTo);
        yStart = Math.min(yFrom, yTo);
        yEnd = Math.max(yFrom, yTo);

        for (xStart = xStart + 1, yEnd = yEnd - 1; xStart < xStop
                && yEnd > yStart; xStart++, yEnd--) {
            if (!(board.getElementAt(xStart, yEnd) instanceof Field))
                return false;
        }

        return true;
    }

}
