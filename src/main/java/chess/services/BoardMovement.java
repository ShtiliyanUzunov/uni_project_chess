package chess.services;

import chess.Board;
import chess.figures.*;
import chess.services.CollisionChecker;
import chess.services.GlobalContext;
import chess.util.NotPureFunction;
import chess.util.Patterns;
import chess.util.PureFunction;
import communication.ChannelNames;
import communication.EventBus;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
public class BoardMovement {

    private EventBus eventBus = EventBus.getEventBus();
    private CollisionChecker collisionChecker = new CollisionChecker();
    private List<Function<Void, Void>> moveCallbacks = new ArrayList<>();

    // Main Move Function
    @NotPureFunction
    public void moveFigure(int xFrom, int yFrom, int xTo, int yTo) {
        long start = System.currentTimeMillis();
        Board board = GlobalContext.getBoard();

        clearMoveCallbacks();
        Figure figure = board.getElementAt(xFrom, yFrom);

        if (figure instanceof Field || !isMoveValid(xFrom, yFrom, xTo, yTo, true)) {
            return;
        }

        applyMoveCallbacks();

        board.setPlayerTurn(opositeColor(xFrom, yFrom));
        board.setLastMove(new int[]{xFrom, yFrom, xTo, yTo});

        figure.setMoved(true);
        board.setElementAt(xTo, yTo, figure);
        board.setElementAt(xFrom, yFrom, new Field());
        eightRank(xTo, yTo);

        eventBus.post(ChannelNames.MOVE_FINISHED, null);
        long end = System.currentTimeMillis();
        System.out.printf("Move exec time: %d%n", end - start);
    }

    @NotPureFunction
    private void applyMoveCallbacks() {
        moveCallbacks.forEach(x -> {
            x.apply(null);
        });
    }

    @PureFunction
    private void clearMoveCallbacks() {
        moveCallbacks = new ArrayList<>();
    }

    @PureFunction
    public boolean isMoveValid(int xFrom, int yFrom, int xTo, int yTo, boolean addCallbacks) {
        Board board = GlobalContext.getBoard();

        if (!validCoordinates(xFrom, yFrom, xTo, yTo)) {
            return false;
        }

        boolean specialMoveCondition = nPassant(xFrom, yFrom, xTo, yTo, addCallbacks)
                || casteling(xFrom, yFrom, xTo, yTo, addCallbacks);

        boolean isNotCheckedAfterMove = isNotCheckedAfterMove(xFrom, yFrom, xTo, yTo);
        boolean normalMoveCondition =
                isPathClear(xFrom, yFrom, xTo, yTo)
                        && !isTargetFigureFriendly(xFrom, yFrom, xTo, yTo)
                        && board.getElementAt(xFrom, yFrom).isTargetLocationValid(xTo, yTo)
                        && checkTurn(xFrom, yFrom);

        return isNotCheckedAfterMove && (normalMoveCondition || specialMoveCondition);
    }

    @PureFunction
    public boolean validCoordinates(int xFrom, int yFrom, int xTo, int yTo) {
        return indexInBoardRange(xFrom) && indexInBoardRange(yFrom) && indexInBoardRange(xTo)
                && indexInBoardRange(yTo);
    }

    @PureFunction
    private boolean indexInBoardRange(int index) {
        return 0 <= index && index <= 7;
    }

    @PureFunction
    private boolean isNotCheckedAfterMove(int xFrom, int yFrom, int xTo, int yTo) {
        Board board = GlobalContext.getBoard();

        Figure figureSource = board.getElementAt(xFrom, yFrom);
        Figure figureTarget = board.getElementAt(xTo, yTo);

        board.setElementAt(xFrom, yFrom, new Field());
        board.setElementAt(xTo, yTo, figureSource);

        Figure king = board.getKingInTurn();

        List<Figure> figuresDiag = new ArrayList<>(Patterns.selectUsingDiagonalPatternFromPosition(king.getX(), king.getY()));
        List<Figure> figuresHandV = new ArrayList<>(Patterns.selectUsingHorizontalAndVerticalPatternFromPosition(king.getX(), king.getY()));
        List<Figure> figuresKn = new ArrayList<>(Patterns.selectUsingKnightPatternFromPosition(king.getX(), king.getY()));
        List<Figure> figuresPawn = new ArrayList<>(Patterns.selectUsingPawnAttackPatternFromPosition(king.getX(), king.getY()));

        int enemyFigures = 0;
        enemyFigures += countFiguresByClass(figuresDiag,
                Arrays.asList(new Class<?>[]{Bishop.class, Queen.class}), figureSource.getColor());
        enemyFigures += countFiguresByClass(figuresHandV,
                Arrays.asList(new Class<?>[]{Rook.class, Queen.class}), figureSource.getColor());
        enemyFigures += countFiguresByClass(figuresKn,
                Arrays.asList(new Class<?>[]{Knight.class}), figureSource.getColor());
        enemyFigures += countFiguresByClass(figuresPawn,
                Arrays.asList(new Class<?>[]{Pawn.class}), figureSource.getColor());

        board.setElementAt(xFrom, yFrom, figureSource);
        board.setElementAt(xTo, yTo, figureTarget);

        return enemyFigures == 0;
    }

    @PureFunction
    private int countFiguresByClass(List<Figure> list, List<Class<?>> figClasses, String color) {
        return list.stream().map(fig -> {
            if (!(figClasses.contains(fig.getClass())))
                return 0;

            if (!fig.getColor().equalsIgnoreCase(color))
                return 1;
            return 0;
        }).reduce(0, Integer::sum);
    }

    @PureFunction
    public boolean isPathClear(int xFrom, int yFrom, int xTo, int yTo) {
        if (!validCoordinates(xFrom, yFrom, xTo, yTo)) {
            return false;
        }

        return collisionChecker.isPathClear(xFrom, yFrom, xTo, yTo);
    }

    @PureFunction
    private boolean checkTurn(int xFrom, int yFrom) {
        Board board = GlobalContext.getBoard();
        return board.getElementAt(xFrom, yFrom).getColor().equalsIgnoreCase(board.getPlayerTurn());
    }

    @PureFunction
    private String opositeColor(int xFrom, int yFrom) {
        Board board = GlobalContext.getBoard();
        if (board.getElementAt(xFrom, yFrom).getColor().equalsIgnoreCase("White"))
            return "Black";
        else
            return "White";
    }

    @PureFunction
    private boolean isTargetFigureFriendly(int xFrom, int yFrom, int xTo, int yTo) {
        Board board = GlobalContext.getBoard();
        if (board.getElementAt(xTo, yTo) instanceof Field)
            return false;
        return board.getElementAt(xFrom, yFrom).getColor().equalsIgnoreCase(board.getElementAt(xTo, yTo)
                .getColor());
    }

    // Special Moves Functions
    @PureFunction
    private boolean casteling(int xFrom, int yFrom, int xTo, int yTo, boolean addCallback) {
        Board board = GlobalContext.getBoard();

        Figure king = board.getElementAt(xFrom, yFrom);

        if (king.isMoved() ||
                king.isAttByOpponent() ||
                !(king instanceof King) ||
                !(Math.abs(xFrom - xTo) == 2 && yFrom == yTo)
        )
            return false;


        String playerColor = board.getElementAt(xFrom, yFrom).getColor();
        Figure[] fields;
        Figure rook;
        int rookTargetX, rookSourceX;

        if (xFrom > xTo) {
            // Queen Side Casteling
            fields = new Figure[]{
                    board.getElementAt(xFrom - 1, yFrom),
                    board.getElementAt(xFrom - 2, yFrom),
                    board.getElementAt(xFrom - 3, yFrom)
            };
            rookSourceX = 0;
            rookTargetX = 3;
        } else {
            // King Side Casteling
            fields = new Figure[]{
                    board.getElementAt(xFrom + 1, yFrom),
                    board.getElementAt(xFrom + 2, yFrom)
            };
            rookTargetX = 5;
            rookSourceX = 7;
        }

        rook = board.getElementAt(rookSourceX, yFrom);
        boolean condSideEmpty = Arrays.stream(fields).map(x -> {
            if (x instanceof Field)
                return 0;
            return 1;
        }).reduce(0, Integer::sum) == 0;

        boolean condSideNotAtt = Arrays.stream(fields).map(x -> {
            if (!x.isAttByOpponent(playerColor))
                return 0;
            return 1;
        }).reduce(0, Integer::sum) == 0;

        boolean rookNotMoved = !rook.isMoved() && rook instanceof Rook;

        if (condSideEmpty && condSideNotAtt && rookNotMoved) {
            if (addCallback) {
                moveCallbacks.add((Void param) -> {
                    board.setElementAt(rookTargetX, yFrom, rook);
                    board.setElementAt(rookSourceX, yFrom, new Field());
                    return null;
                });
            }

            return true;
        }

        return false;
    }

    @NotPureFunction
    private void eightRank(int x2, int y2) {
        Board board = GlobalContext.getBoard();

        if (!(board.getElementAt(x2, y2) instanceof Pawn)) {
            return;
        }
        if ((board.getElementAt(x2, y2).getColor().equalsIgnoreCase("White") && y2 == 7)
                || (board.getElementAt(x2, y2).getColor().equalsIgnoreCase("Black") && y2 == 0)) {

            String[] choice = new String[4];
            choice[0] = "Queen";
            choice[1] = "Rook";
            choice[2] = "Knight";
            choice[3] = "Bishop";

            int a = JOptionPane.showOptionDialog(null, "Chess",
                    "Choose a Figure", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, choice, null);

            switch (a) {
                case -1:
                    eightRank(x2, y2);
                    break;
                case 0: {
                    board.setElementAt(x2, y2, new Queen(board.getElementAt(x2, y2)
                            .getColor(), x2, y2, board));
                    board.getElementAt(x2, y2).setPosition(x2, y2);
                    break;
                }
                case 1: {
                    board.setElementAt(x2, y2, new Rook(board.getElementAt(x2, y2)
                            .getColor(), x2, y2, board));
                    board.getElementAt(x2, y2).setPosition(x2, y2);
                    break;
                }
                case 2: {
                    board.setElementAt(x2, y2, new Knight(board
                            .getElementAt(x2, y2).getColor(), x2, y2, board));
                    board.getElementAt(x2, y2).setPosition(x2, y2);
                    break;
                }
                case 3: {
                    board.setElementAt(x2, y2, new Bishop(board
                            .getElementAt(x2, y2).getColor(), x2, y2, board));
                    board.getElementAt(x2, y2).setPosition(x2, y2);
                    break;
                }
            }
        }

    }

    @PureFunction
    private boolean nPassant(int xFrom, int yFrom, int xTo, int yTo, boolean addCallback) {
        Board board = GlobalContext.getBoard();
        int[] lastMove = board.getLastMove();

        if (!(board.getElementAt(xFrom, yFrom) instanceof Pawn))
            return false;

        int pawnMoveVector = board.getPlayerTurn().equalsIgnoreCase("White") ? 1 : -1;
        int rankRequired = board.getPlayerTurn().equalsIgnoreCase("White") ? 4 : 3;
        boolean currentMoveCondition = (yTo - yFrom) * pawnMoveVector == 1 && (Math.abs(xFrom - xTo) == 1)
                && yFrom == rankRequired;
        boolean lastMoveCondition = (board.getElementAt(lastMove[2], lastMove[3]) instanceof Pawn)
                && (Math.abs(lastMove[1] - lastMove[3]) == 2)
                && (xTo == lastMove[2]);

        if (currentMoveCondition && lastMoveCondition) {
            if (addCallback) {
                moveCallbacks.add((Void obj) -> {
                    board.setElementAt(lastMove[2], lastMove[3], new Field());
                    return null;
                });
            }

            return true;
        }

        return false;
    }

}
