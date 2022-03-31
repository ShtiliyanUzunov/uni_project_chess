package chess;

import chess.services.GlobalContext;
import communication.ChannelNames;
import communication.EventBus;
import chess.figures.*;
import graphics.ChessPanel;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;

@Getter
@Setter
public class BoardMovement {

    private EventBus eventBus = EventBus.getEventBus();

    //Last Move[0] = last x, Last Move[1] = last y, Last Move[2] = new x, Last Move[3] = new y
    private int[] lastMove;
    private String playerTurn;

    public BoardMovement() {
        this.resetState();
    }

    // Main Move Function
    public void moveFigure(int xFrom, int yFrom, int xTo, int yTo) {
        Board board = GlobalContext.getBoard();
        if (board.getElementAt(xFrom, yFrom) instanceof Field) {
            ChessPanel.setCords(-1, -1, -1, -1);
            return;
        }

        boolean specialMoveCondition = nPassant(xFrom, yFrom, xTo, yTo)
                || casteling(xFrom, yFrom, xTo, yTo);


        boolean clearPathCondition =
                isPathClear(xFrom, yFrom, xTo, yTo)
                        && !isTargetFigureFriendly(xFrom, yFrom, xTo, yTo)
                        && board.getElementAt(xFrom, yFrom).isTargetLocationValid(xTo, yTo)
                        && checkTurn(xFrom, yFrom)
                        && discoverCheck(xFrom, yFrom, xTo, yTo);

        if (clearPathCondition || specialMoveCondition) {
            setLastMove(new int[]{xFrom, yFrom, xTo, yTo});

            playerTurn = opositeColor(xFrom, yFrom);
            board.getElementAt(xFrom, yFrom).setMoved(true);
            board.getElementAt(xFrom, yFrom).setPosition(xTo, yTo);
            board.setElementAt(xTo, yTo, board.getElementAt(xFrom, yFrom));
            board.setElementAt(xFrom, yFrom, new Field());
            eightRank(xTo, yTo);

            board.markBoardAttacks();

            if (kingChecked()) {
                EventBus.getEventBus().post(ChannelNames.UI_CHECK, null);
            }
        }
    }

    private boolean discoverCheck(int xFrom, int yFrom, int xTo, int yTo) {
        Board board = GlobalContext.getBoard();

        if (board.getElementAt(xFrom, yFrom) instanceof King) {
            Figure temp1 = board.getElementAt(xFrom, yFrom);
            Figure temp2 = board.getElementAt(xTo, yTo);

            board.setElementAt(xFrom, yFrom, new Field());
            board.setElementAt(xTo, yTo, temp1);
            board.markBoardAttacks();

            if (board.getElementAt(xTo, yTo).isAttByOpponent()) {
                board.setElementAt(xFrom, yFrom, temp1);
                board.setElementAt(xTo, yTo, temp2);
                board.markBoardAttacks();
                return false;
            } else {
                board.setElementAt(xFrom, yFrom, temp1);
                board.setElementAt(xTo, yTo, temp2);
                board.markBoardAttacks();
                return true;
            }
        }

        Figure temp1 = board.getElementAt(xFrom, yFrom);
        Figure temp2 = board.getElementAt(xTo, yTo);
        String color = board.getElementAt(xFrom, yFrom).getColor();

        board.setElementAt(xFrom, yFrom, new Field());
        board.setElementAt(xTo, yTo, temp1);
        board.markBoardAttacks();

        if (color.equalsIgnoreCase("White")) {
            if (board.getWhiteKing().isAttByOpponent()) {
                board.setElementAt(xFrom, yFrom, temp1);
                board.setElementAt(xTo, yTo, temp2);
                board.markBoardAttacks();
                return false;
            } else {
                board.setElementAt(xFrom, yFrom, temp1);
                board.setElementAt(xTo, yTo, temp2);
                board.markBoardAttacks();
                return true;
            }
        } else {
            if (board.getBlackKing().isAttByOpponent()) {
                board.setElementAt(xFrom, yFrom, temp1);
                board.setElementAt(xTo, yTo, temp2);
                board.markBoardAttacks();
                return false;
            } else {
                board.setElementAt(xFrom, yFrom, temp1);
                board.setElementAt(xTo, yTo, temp2);
                board.markBoardAttacks();
                return true;
            }
        }

    }

    private boolean kingChecked() {
        Board board = GlobalContext.getBoard();

        Figure king = playerTurn.equalsIgnoreCase("white") ?
                board.getWhiteKing() : board.getBlackKing();

        if (king.isAttByOpponent()) {
            return true;
        }

        return false;
    }

    public boolean isPathClear(int xFrom, int yFrom, int xTo, int yTo) {
        Board board = GlobalContext.getBoard();

        if (board.getElementAt(xFrom, yFrom) instanceof Knight)
            return true;

        int temp1, temp2, temp3, temp4;
        if (xFrom == xTo) {
            temp1 = Math.min(yFrom, yTo);
            temp2 = Math.max(yFrom, yTo);
            for (temp1 = temp1 + 1; temp1 < temp2; temp1++) {
                if (!(board.getElementAt(xFrom, temp1) instanceof Field))
                    return false;
            }
        }

        if (yFrom == yTo) {
            temp1 = Math.min(xFrom, xTo);
            temp2 = Math.max(xFrom, xTo);
            for (temp1 = temp1 + 1; temp1 < temp2; temp1++) {
                if (!(board.getElementAt(temp1, yFrom) instanceof Field))
                    return false;
            }
        }

        if (((xFrom > xTo) && (yFrom > yTo)) || ((xFrom < xTo) && (yFrom < yTo))) {
            if (xFrom > xTo) {
                temp1 = xTo;
                temp2 = xFrom;
                temp3 = yTo;
                temp4 = yFrom;
            } else {
                temp1 = xFrom;
                temp2 = xTo;
                temp3 = yFrom;
                temp4 = yTo;
            }
            for (temp1 = temp1 + 1, temp3 = temp3 + 1; temp1 < temp2
                    && temp3 < temp4; temp1++, temp3++) {
                if (!(board.getElementAt(temp1, temp3) instanceof Field))
                    return false;
            }
        }

        if (((xFrom > xTo) && (yFrom < yTo)) || ((xFrom < xTo) && (yFrom > yTo))) {
            // temp1 - po-malkoto x
            // temp 2 -po-golqmoto x
            // temp 3 - po-malkoto y
            // temp 4 - pogolqmoto y
            if (xFrom > xTo) {
                temp1 = xTo;
                temp2 = xFrom;
                temp3 = yFrom;
                temp4 = yTo;
            } else {
                temp1 = xFrom;
                temp2 = xTo;
                temp3 = yTo;
                temp4 = yFrom;
            }
            for (temp1 = temp1 + 1, temp4 = temp4 - 1; temp1 < temp2
                    && temp4 > temp3; temp1++, temp4--) {
                if (!(board.getElementAt(temp1, temp4) instanceof Field))
                    return false;
            }
        }

        return true;
    }

    private boolean checkTurn(int xFrom, int yFrom) {
        Board board = GlobalContext.getBoard();
        return board.getElementAt(xFrom, yFrom).getColor().equalsIgnoreCase(playerTurn);
    }

    private String opositeColor(int xFrom, int yFrom) {
        Board board = GlobalContext.getBoard();
        if (board.getElementAt(xFrom, yFrom).getColor().equalsIgnoreCase("White"))
            return "Black";
        else
            return "White";
    }

    private boolean isTargetFigureFriendly(int xFrom, int yFrom, int xTo, int yTo) {
        Board board = GlobalContext.getBoard();
        if (board.getElementAt(xTo, yTo) instanceof Field)
            return false;
        return board.getElementAt(xFrom, yFrom).getColor().equalsIgnoreCase(board.getElementAt(xTo, yTo)
                .getColor());
    }

    // Special Moves Functions
    private boolean casteling(int xFrom, int yFrom, int xTo, int yTo) {
        Board board = GlobalContext.getBoard();
        if (!(board.getElementAt(xFrom, yFrom) instanceof King))
            return false;
        if (board.getElementAt(xFrom, yFrom).isMoved())
            return false;
        if (board.getElementAt(xFrom, yFrom).isAttByOpponent(
                board.getElementAt(xFrom, yFrom).getColor()))
            return false;
        if (!(Math.abs(xFrom - xTo) == 2 && yFrom == yTo))
            return false;
        boolean condSideEmpty;
        boolean condSideNotAtt;
        boolean rookNotMoved;

        // Queen Side Casteling
        if (xFrom > xTo) {
            condSideEmpty = board.getElementAt(xFrom - 1, yFrom) instanceof Field
                    && board.getElementAt(xFrom - 2, yFrom) instanceof Field
                    && board.getElementAt(xFrom - 3, yFrom) instanceof Field;
            condSideNotAtt = !board.getElementAt(xFrom - 1, yFrom).isAttByOpponent(
                    board.getElementAt(xFrom, yFrom).getColor())
                    && !board.getElementAt(xFrom - 2, yFrom).isAttByOpponent(
                    board.getElementAt(xFrom, yFrom).getColor())
                    && !board.getElementAt(xFrom - 3, yFrom).isAttByOpponent(
                    board.getElementAt(xFrom, yFrom).getColor());
            rookNotMoved = !board.getElementAt(0, yFrom).isMoved()
                    && board.getElementAt(0, yFrom) instanceof Rook;
            if (condSideEmpty && condSideNotAtt && rookNotMoved) {
                board.setElementAt(3, yFrom, board.getElementAt(0, yFrom));
                board.setElementAt(0, yFrom, new Field());
                return true;
            }
        }
        // King Side Casteling
        if (xFrom < xTo) {
            condSideEmpty = board.getElementAt(xFrom + 1, yFrom) instanceof Field
                    && board.getElementAt(xFrom + 2, yFrom) instanceof Field;
            condSideNotAtt = !board.getElementAt(xFrom + 1, yFrom).isAttByOpponent(
                    board.getElementAt(xFrom, yFrom).getColor())
                    && !board.getElementAt(xFrom + 2, yFrom).isAttByOpponent(
                    board.getElementAt(xFrom, yFrom).getColor());
            rookNotMoved = !board.getElementAt(7, yFrom).isMoved()
                    && board.getElementAt(7, yFrom) instanceof Rook;
            if (condSideEmpty && condSideNotAtt && rookNotMoved) {
                board.setElementAt(5, yFrom, board.getElementAt(7, yFrom));
                board.getElementAt(5, yFrom).setPosition(5, yFrom);
                board.setElementAt(7, yFrom, new Field());
                return true;
            }
        }

        return false;
    }


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

    private boolean nPassant(int xFrom, int yFrom, int xTo, int yTo) {
        Board board = GlobalContext.getBoard();

        boolean positionCondition;
        boolean lastMoveCondition;
        if (!(board.getElementAt(xFrom, yFrom) instanceof Pawn))
            return false;
        if (board.getElementAt(xFrom, yFrom).getColor().equalsIgnoreCase("White")) {
            positionCondition = (yFrom == 4) && (yTo == 5) && (Math.abs(xFrom - xTo) == 1);
            lastMoveCondition = (board.getElementAt(lastMove[2], lastMove[3]) instanceof Pawn)
                    && (Math.abs(lastMove[1] - lastMove[3]) == 2)
                    && (xTo == lastMove[2]);
            if (positionCondition && lastMoveCondition) {
                board.setElementAt(lastMove[2], lastMove[3], new Field());
                return true;
            }
        } else {
            positionCondition = (yFrom == 3) && (yTo == 2) && (Math.abs(xFrom - xTo) == 1);
            lastMoveCondition = (board.getElementAt(lastMove[2], lastMove[3]) instanceof Pawn)
                    && (Math.abs(lastMove[1] - lastMove[3]) == 2 && (xTo == lastMove[2]));
            if (positionCondition && lastMoveCondition) {
                board.setElementAt(lastMove[2], lastMove[3], new Field());
                return true;
            }
        }
        return false;
    }

    public void resetState() {
        this.setLastMove(new int[4]);
        this.setPlayerTurn("White");
    }

}
